package cn.guankejian.test

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * 解决VP2的滑动冲突
 *
 */
class NestedScrollableHostForVP : CoordinatorLayout {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

  private lateinit var vp2: ViewPager2
  private var disallowParentInterceptDownEvent = true
  private var startX = 0
  private var startY = 0

  override fun onFinishInflate() {
    super.onFinishInflate()
    for (i in 0 until childCount) {
      val childView = getChildAt(i)
      if (childView is ViewPager2) {
        vp2 = childView
        break;
      }
    }
    if (vp2 == null)
      throw IllegalStateException("must have a child is ViewPager2")
  }


  var lastMoveMillion: Long = System.currentTimeMillis()



  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    val doNotNeedIntercept = (
            !vp2.isUserInputEnabled ||
                    (vp2.adapter != null && vp2.adapter?.itemCount!! <= 1))
    if (doNotNeedIntercept) {
      return super.onInterceptTouchEvent(ev)
    }

    when (ev.action) {
      MotionEvent.ACTION_DOWN -> {
        startX = ev.x.toInt()
        "ArticleVP -> DOWN   vp2.currentItem = ${vp2.currentItem}".logE()
        val cur = System.currentTimeMillis()
        lastMoveMillion = cur
        startY = ev.y.toInt()
        parent.requestDisallowInterceptTouchEvent(true) //Down时候 不需要交给child

      }
      MotionEvent.ACTION_MOVE -> {
        "ArticleVP -> MOVE  vp2.currentItem = ${vp2.currentItem}".logE()
        val endX = ev.x.toInt()
        val endY = ev.y.toInt()
        val disX = abs(endX - startX)
        val disY = abs(endY - startY)
        if (vp2.orientation == ViewPager2.ORIENTATION_VERTICAL) {
          onVerticalActionMove(endY, disX, disY)
        } else if (vp2.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
          onHorizontalActionMove(endX, disX, disY)
        }
      }
      MotionEvent.ACTION_UP->{
        "ArticleVP -> UP  ".logE()
      }
      MotionEvent.ACTION_CANCEL -> {
        "ArticleVP -> CANCEL  ".logE()
      }
    }
    return super.onInterceptTouchEvent(ev)
  }

  private fun onHorizontalActionMove(endX: Int, disX: Int, disY: Int) {
    if (vp2.adapter == null) {
      return
    }
    if (disX > disY) {
      val currentItem = vp2.currentItem
      val itemCount = vp2.adapter!!.itemCount
      if (itemCount > 1 && currentItem == itemCount - 2) {
        parent.requestDisallowInterceptTouchEvent(true)
      }
      else if (currentItem == 0) {
        if (endX - startX > 0) {
          parent.requestDisallowInterceptTouchEvent(false)
        } else {
          parent.requestDisallowInterceptTouchEvent(true)
        }
      }
      else if (vp2.currentItem == itemCount - 1) {
        if (endX - startX > 0) {
          parent.requestDisallowInterceptTouchEvent(true)
        } else {
          parent.requestDisallowInterceptTouchEvent(false)
        }
      }
      //其余中间的 就自己处理
      else {
        parent.requestDisallowInterceptTouchEvent(true)
      }
    } else if (disY > disX) {
      parent.requestDisallowInterceptTouchEvent(false)
    }
  }

  private fun onVerticalActionMove(endY: Int, disX: Int, disY: Int) {
    if (vp2.adapter == null) {
      return
    }
    val currentItem = vp2.currentItem
    val itemCount = vp2.adapter!!.itemCount
    if (disY > disX) {
      if (currentItem == 0 && endY - startY > 0) {
        parent.requestDisallowInterceptTouchEvent(false)
      } else {
        parent.requestDisallowInterceptTouchEvent(
          currentItem != itemCount - 1
                  || endY - startY >= 0
        )
      }
    } else if (disX > disY) {
      parent.requestDisallowInterceptTouchEvent(false)
    }
  }


}