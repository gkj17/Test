package cn.guankejian.test

import android.graphics.Bitmap
import android.os.Handler
import android.view.PixelCopy
import android.view.SurfaceHolder
import android.view.SurfaceView


/**
 *  Created by guank on 2023/9/8 23:28
 *  Email       :   guan_kejian@163.com
 *  Version     :   1.0
 *  Description :
 **/

open interface ScreenShotCallback {
  fun success(bitmap: Bitmap)
}
fun SurfaceView.screenshot(callback: ScreenShotCallback){

  val holder: SurfaceHolder = this.holder

  //需要截取的长和宽
  val outWidth = this.width
  val outHeight = this.height
  val mScreenBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888)
  val listener = PixelCopy.OnPixelCopyFinishedListener { copyResult ->
    if (copyResult == PixelCopy.SUCCESS) {
      // 截图成功，可以使用截取到的 screenshotBitmap 进行进一步的处理
      callback.success(mScreenBitmap)
    } else {
      // 截图失败，处理错误情况\
    }
  }

// 调用 PixelCopy 的 request 方法进行截图
  PixelCopy.request(holder.surface, mScreenBitmap,listener, Handler())

}