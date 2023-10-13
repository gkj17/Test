package cn.guankejian.test

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

class LanJingMovingDraggable(
  private val windowWidth: Float,
  private val windowHeight: Float,
  private val lanjingWidth: Float,
  private val lanjingHeight: Float
) : BaseDraggable() {
  private val horizontalGap: Float
  private val vertialGap: Float

  /** 手指按下的坐标  */
  private var mViewDownX = 0f
  private var mViewDownY = 0f

  /** 触摸移动标记  */
  private var mMoveTouch = false
  private var touchInLanjing = false

  init {
    horizontalGap = (windowWidth - lanjingWidth) / 2
    vertialGap = (windowHeight - lanjingHeight) / 2
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouch(v: View, event: MotionEvent): Boolean {
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        // 记录按下的位置（相对 View 的坐标）
        mViewDownX = event.x
        mViewDownY = event.y
        touchInLanjing =
          mViewDownX > horizontalGap && mViewDownX < horizontalGap + lanjingWidth && mViewDownY > vertialGap && mViewDownY < vertialGap + lanjingHeight
        if (touchInLanjing) {
          println("点击了蓝鲸里面")
        } else {
          println("点击了蓝鲸【外面】")
        }
        mMoveTouch = false
      }

      MotionEvent.ACTION_MOVE -> {
        if (touchInLanjing) {
          return false
        }
        // 记录移动的位置（相对屏幕的坐标）
        val rawMoveX = event.rawX - windowInvisibleWidth
        val rawMoveY = event.rawY - windowInvisibleHeight
        var newX = rawMoveX - mViewDownX
        var newY = rawMoveY - mViewDownY
        if (newX < 0) {
          newX = 0f
        }
        if (newY < 0) {
          newY = 0f
        }

        // 更新移动的位置
        updateLocation(newX, newY)
        if (!mMoveTouch && isFingerMove(mViewDownX, event.x, mViewDownY, event.y)) {
          // 如果用户移动了手指，那么就拦截本次触摸事件，从而不让点击事件生效
          mMoveTouch = true
        }
      }

      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> return mMoveTouch
      else -> {}
    }
    return false
  }
}