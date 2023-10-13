package cn.guankejian.test

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewGroup.VISIBLE
import android.view.WindowManager
import android.view.WindowManager.BadTokenException
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import cn.guankejian.test.ui.MainActivity


class  ComplexWindow  constructor(context: Context) :
  ScreenOrientationMonitor.OnScreenOrientationCallback {
  companion object{
    private val instances:MutableList<ComplexWindow> = ArrayList()
    private val HANDLER: Handler = Handler(Looper.getMainLooper())
    @JvmStatic
    fun with(application: Application): ComplexWindow {
      return ComplexWindow(application)
    }
  }

  private var mScreenOrientationMonitor: ScreenOrientationMonitor? = null

  private lateinit var _windowManager: WindowManager
  lateinit var windowParams: WindowManager.LayoutParams
    private set
  private lateinit var _context: Context
  lateinit var decorView: ViewGroup
    private set
  private var _draggable: BaseDraggable? = null
  private var _listener: OnWindowLifecycle? = null

  private var _duration = 0L

  var showing = false
    private set

  init{
    _context = context
    decorView = WindowLayout(context)
    _windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    // 配置一些默认的参数
    windowParams = WindowManager.LayoutParams()
    windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT
    windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT
    windowParams.format = PixelFormat.TRANSLUCENT
    windowParams.windowAnimations = android.R.style.Animation_Toast
    windowParams.packageName = context.packageName
    // 设置触摸外层布局（除 WindowManager 外的布局，默认是 WindowManager 显示的时候外层不可触摸）
    // 需要注意的是设置了 FLAG_NOT_TOUCH_MODAL 必须要设置 FLAG_NOT_FOCUSABLE，否则就会导致用户按返回键无效
    windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    // 将当前实例添加到静态集合中
    instances.add(this)
  }

  constructor(activity:Activity):this(activity.applicationContext){

  }

  constructor(fragment:Fragment):this(fragment.requireActivity()){

  }

  constructor(application: Application):this(application.applicationContext){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      setWindowType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
    } else {
      setWindowType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }
  }

  fun setContentView(id: Int): ComplexWindow {
    return setContentView(LayoutInflater.from(_context).inflate(id, decorView, false))
  }


  fun setDraggable(draggable: BaseDraggable): ComplexWindow {
    _draggable = draggable
    if (draggable != null) {
      // 如果当前是否设置了不可触摸，如果是就擦除掉这个标记
      removeWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
      // 如果当前是否设置了可移动窗口到屏幕之外，如果是就擦除这个标记
      removeWindowFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
      if (showing) {
        update()
        draggable.start(this)
      }
    }
    if (mScreenOrientationMonitor == null) {
      mScreenOrientationMonitor = ScreenOrientationMonitor(_context.getResources().getConfiguration())
    }
    mScreenOrientationMonitor?.registerCallback(_context, this)
    return this
  }

  /**
   * 移除窗口标记
   */
  fun removeWindowFlags(flags: Int): ComplexWindow {
    windowParams.flags = windowParams.flags and flags.inv()
    postUpdate()
    return this
  }
  fun setWidth(width: Int): ComplexWindow {
    windowParams.width = width
    if (decorView.childCount > 0) {
      val contentView: View = decorView.getChildAt(0)
      val layoutParams = contentView.layoutParams
      if (layoutParams != null && layoutParams.width != width) {
        layoutParams.width = width
        contentView.layoutParams = layoutParams
      }
    }
    postUpdate()
    return this
  }

  /**
   * 设置悬浮窗高度
   */
  fun setHeight(height: Int): ComplexWindow {
    windowParams.height = height
    if (decorView.childCount > 0) {
      val contentView: View = decorView.getChildAt(0)
      val layoutParams = contentView.layoutParams
      if (layoutParams != null && layoutParams.height != height) {
        layoutParams.height = height
        contentView.layoutParams = layoutParams
      }
    }
    postUpdate()
    return this
  }

  /**
   * 设置悬浮窗显示的重心
   */
  fun setGravity(gravity: Int): ComplexWindow {
    windowParams.gravity = gravity
    postUpdate()
    post {
      _draggable?.refreshLocationCoordinate()
    }
    return this
  }

  fun setContentView(view: View): ComplexWindow {
    if (decorView.childCount > 0) {
      decorView.removeAllViews()
    }
    decorView.addView(view)
    val layoutParams = view.layoutParams
    if (layoutParams is MarginLayoutParams) {
      val marginLayoutParams = layoutParams as MarginLayoutParams
      // 清除 Margin，因为 WindowManager 没有这一属性可以设置，并且会跟根布局相冲突
      marginLayoutParams.topMargin = 0
      marginLayoutParams.bottomMargin = 0
      marginLayoutParams.leftMargin = 0
      marginLayoutParams.rightMargin = 0
    }

    // 如果当前没有设置重心，就自动获取布局重心
    if (windowParams.gravity == Gravity.NO_GRAVITY) {
      if (layoutParams is FrameLayout.LayoutParams) {
        val gravity = layoutParams.gravity
        if (gravity != FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY) {
          windowParams.gravity = gravity
        }
      } else if (layoutParams is LinearLayout.LayoutParams) {
        val gravity = layoutParams.gravity
        if (gravity != FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY) {
          windowParams.gravity = gravity
        }
      }
      if (windowParams.gravity == Gravity.NO_GRAVITY) {
        // 默认重心是居中
        windowParams.gravity = Gravity.CENTER
      }
    }
    if (layoutParams != null) {
      if (windowParams.width == WindowManager.LayoutParams.WRAP_CONTENT &&
        windowParams.height == WindowManager.LayoutParams.WRAP_CONTENT
      ) {
        // 如果当前 Dialog 的宽高设置了自适应，就以布局中设置的宽高为主
        windowParams.width = layoutParams.width
        windowParams.height = layoutParams.height
      } else {
        // 如果当前通过代码动态设置了宽高，则以动态设置的为主
        layoutParams.width = windowParams.width
        layoutParams.height = windowParams.height
      }
    }
    postUpdate()
    return this
  }



  fun show() {
    require(decorView.childCount != 0) { "WindowParams and view cannot be empty" }



    // 如果当前已经显示则进行更新
    if (showing) {
      update()
      return
    }



    if (_context is Activity) {
      val activity = _context as Activity
      if (activity.isFinishing || activity.isDestroyed) {
        return
      }
    }
    try {
      // 如果 View 已经被添加的情况下，就先把 View 移除掉
      if (decorView.parent != null) {
        _windowManager.removeViewImmediate(decorView)
      }
      _windowManager.addView(decorView, windowParams)
      // 当前已经显示
      showing = true
      // 如果当前限定了显示时长
      if (_duration != 0L) {
        removeCallbacks { show() }
        postDelayed({ show() }, _duration)
      }
      // 如果设置了拖拽规则
      _draggable?.start(this)

      // 回调监听
      _listener?.onWindowShow(this)
    } catch (e: NullPointerException) {
      // 如果这个 View 对象被重复添加到 WindowManager 则会抛出异常
      // java.lang.IllegalStateException: View has already been added to the window manager.
      e.printStackTrace()
    } catch (e: IllegalStateException) {
      e.printStackTrace()
    } catch (e: java.lang.IllegalArgumentException) {
      e.printStackTrace()
    } catch (e: BadTokenException) {
      e.printStackTrace()
    }
  }

  private fun setWindowType(type: Int): ComplexWindow {
    windowParams.type = type
    postUpdate()
    return this
  }

  private val mUpdateRunnable: Runnable = Runnable {
    update()
  }

  fun update() {
    if (!showing) {
      return
    }
    try {
      // 更新 WindowManger 的显示
      _windowManager.updateViewLayout(decorView, windowParams)
    } catch (e: IllegalArgumentException) {
      // 当 WindowManager 已经消失时调用会发生崩溃
      // IllegalArgumentException: View not attached to window manager
      e.printStackTrace()
    }
  }
  private fun postUpdate() {
    if (!showing) {
      return
    }
    removeCallbacks(mUpdateRunnable)
    post(mUpdateRunnable)
  }

  private fun removeCallbacks(runnable: Runnable) {
    HANDLER.removeCallbacks(runnable)
  }

  fun post(runnable: Runnable): Boolean {
    return postDelayed(runnable, 0)
  }

  fun postDelayed(runnable: Runnable, delayMillis: Long): Boolean {
    return postAtTime(runnable, SystemClock.uptimeMillis() + delayMillis.coerceAtLeast(0))
  }

  private fun postAtTime(runnable: Runnable, uptimeMillis: Long): Boolean {
    // 发送和这个 WindowManager 相关的消息回调
    return HANDLER.postAtTime(runnable, this, uptimeMillis)
  }

  fun resume(){
    if(showing)
      return
    showing = true

    decorView.visibility = VISIBLE
    decorView[0].startAnimation(AnimationUtils.loadAnimation(_context, R.anim.slide_in_right))
  }
  fun pause(){
    if(!showing)
      return
    showing = false
    decorView[0].startAnimation(AnimationUtils.loadAnimation(_context, R.anim.slide_in_left).also{
      it.setAnimationListener(object: Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation) {
          //
        }

        override fun onAnimationEnd(p0: Animation) {
          decorView.visibility = View.GONE
        }

        override fun onAnimationRepeat(p0: Animation) {
          //
        }
      })
    })
  }

  override fun onScreenOrientationChange(newOrientation: Int) {
    if (!showing) {
      return
    }
    _draggable?.onScreenOrientationChange()?:return
  }
  interface OnWindowLifecycle {
    /**
     * 显示回调
     */
    fun onWindowShow(window: ComplexWindow) {}

    /**
     * 消失回调
     */
    fun onWindowCancel(window: ComplexWindow) {}

    /**
     * 回收回调
     */
    fun onWindowRecycle(window: ComplexWindow) {}
  }

}