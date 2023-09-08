package cn.guankejian.test

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

object BlurUtil {
//    private const val BLUR_RADIUS = 25f

    fun blur(context: Context, bitmap: Bitmap, radius:Float): Bitmap {
        val inputBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)

        val rs = RenderScript.create(context)
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)

        blurScript.setRadius(radius)
        blurScript.setInput(tmpIn)
        blurScript.forEach(tmpOut)

        tmpOut.copyTo(outputBitmap)

        rs.destroy()

        return outputBitmap
    }
}
