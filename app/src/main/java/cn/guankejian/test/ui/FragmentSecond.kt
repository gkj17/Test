package cn.guankejian.test.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.renderscript.Allocation
import androidx.renderscript.Element
import androidx.renderscript.RenderScript
import androidx.renderscript.ScriptIntrinsicBlur
import cn.guankejian.test.R
import cn.guankejian.test.databinding.FragmentSecondBinding
import cn.guankejian.test.logE
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@ExperimentalPagingApi
@AndroidEntryPoint
class FragmentSecond @Inject constructor(
) : Fragment() {
  private lateinit var binding: FragmentSecondBinding


  val controller: NavController by lazy { findNavController() }


  fun drawImageOnSurface(): Bitmap {
    val bitmap = BitmapFactory.decodeResource(
      resources,
      R.drawable.img2
    )
    val matrix = Matrix()
    val rotationDegrees = 90f
    matrix.postRotate(rotationDegrees)
    return Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
  }


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second, container, false)
//    binding.surface.load(drawImageOnSurface())

    val backgroundImage = drawImageOnSurface()
    val colorImage = Bitmap.createBitmap(backgroundImage.width, backgroundImage.height, Bitmap.Config.ARGB_8888)
    "backgroundImage.height = ${backgroundImage.height} backgroundImage.height = ${backgroundImage.height}".logE()
    colorImage.eraseColor(Color.WHITE) // 设置为纯白色

//    binding.trans.load(R.drawable.white){
//      transformations(BlurTransformation(requireContext(),25f))
//    }

    val rs: RenderScript = RenderScript.create(context)

    val input: Allocation = Allocation.createFromBitmap(rs, colorImage)
    val output: Allocation = Allocation.createTyped(rs, input.getType())

    val script: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    script.setRadius(25f) // 设置模糊半径

    script.setInput(input)
    script.forEach(output)

    val blurredImage = Bitmap.createBitmap(
      backgroundImage.width,
      backgroundImage.height,
      Bitmap.Config.ARGB_8888
    )
    output.copyTo(blurredImage)

    binding.trans.load(blurredImage)
    binding.surface.load(backgroundImage)
    input.destroy()
    output.destroy()
    script.destroy()
    rs.destroy()




    val density = resources.displayMetrics.density
    val offset = (100 * density).toInt()


    binding.icon.setOnClickListener {

      if (!hasClick) {
        blurImg()
        it.startAnimation(TranslateAnimation(0f, offset.toFloat(), 0f, -offset.toFloat()).apply {
          duration = 500
          fillAfter = true
        })
      }
      else {
        it.startAnimation(TranslateAnimation(offset.toFloat(), 0f, -offset.toFloat(), 0f).apply {
          duration = 500
          fillAfter = true
          setAnimationListener(object:Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {

            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

          })
        })
      }
      hasClick = !hasClick

// 启动动画
//            view.startAnimation(animation);
    }
    return binding.root
  }

  private fun applyBlur(blurRadius: Float) {
    lifecycleScope.launch(Dispatchers.Main) {
      "blurRadius =${blurRadius}".logE()
//      binding.trans.load(R.drawable.white){
//        transformations(BlurTransformation(requireContext(),blurRadius))
//      }
    }
  }


  fun blurImg() {
    // 在协程中逐渐增加模糊效果
    val durationMillis = 1000L
    viewLifecycleOwner.lifecycleScope.launch {

      val startTime = System.currentTimeMillis()
      var elapsedTime = 100L

      while (elapsedTime < durationMillis) {
        val progress = elapsedTime.toFloat() / durationMillis
        val currentBlurRadius = (25f) * progress


        withContext(Dispatchers.Main) {
          "加载了 currentBlurRadius = ${currentBlurRadius}".logE()

          applyBlur(currentBlurRadius)
        }

        delay(16) // 每16毫秒更新一次模糊效果
        elapsedTime = System.currentTimeMillis() - startTime
      }
    }
  }

  var hasClick = false

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }


}