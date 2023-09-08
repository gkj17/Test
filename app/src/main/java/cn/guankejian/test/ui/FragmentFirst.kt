package cn.guankejian.test.ui

import android.graphics.*
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.view.animation.TranslateAnimation
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import cn.guankejian.test.*
import cn.guankejian.test.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@ExperimentalPagingApi
@AndroidEntryPoint
class FragmentFirst @Inject constructor(
) : Fragment(){
  private lateinit var binding: FragmentFirstBinding

  private var mediaPlayer: MediaPlayer? = null

  fun drawImage(){
    binding.icon.alpha = 1f

    binding.surface.apply{
       post {
         screenshot(object:ScreenShotCallback{
           override fun success(bitmap: Bitmap) {
             lifecycleScope.launch(Dispatchers.Default) {
               val blurredBitmap = BlurUtil.blur(requireContext(), bitmap, 25f)

               withContext(Dispatchers.Main) {
                 binding.trans.apply{
                   setImageBitmap(blurredBitmap)
                   alpha = 0f
                 }
               }
             }
           }
         })
       }
     }

  }

  fun drawImageOnSurface(holder: SurfaceHolder) {
    val paint = Paint()
    paint.isAntiAlias = true
    paint.style = Paint.Style.STROKE
    val bitmap = BitmapFactory.decodeResource(
      resources,
      R.drawable.img2
    )
    bitmap.logE()
    val matrix = Matrix()

    // 设置旋转角度（以度为单位）
    val rotationDegrees = 90f
    matrix.postRotate(rotationDegrees)
    // 根据旋转矩阵创建一个新的旋转后的 Bitmap 对象
    val rotatedBitmap = Bitmap.createBitmap(
      bitmap,
      0,
      0,
      bitmap.width,
      bitmap.height,
      matrix,
      true
    )
    val canvas: Canvas = holder.lockCanvas() // 先锁定当前surfaceView的画布

    canvas.drawBitmap(rotatedBitmap, 0f, 0f, null) //执行绘制操作

    holder.unlockCanvasAndPost(canvas) // 解除锁定并显示在界面上
  }

  val DURATION = 500L
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false)

//    binding.surface.holder.addCallback(this)



    binding.surface.holder.addCallback(object : SurfaceHolder.Callback {
      override fun surfaceCreated(holder: SurfaceHolder) {
        playVideo("sintel.mp4")
//        drawImageOnSurface(holder)

      }

      override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
      ) {
//        drawImageOnSurface(holder)
      }

      override fun surfaceDestroyed(holder: SurfaceHolder) {
        releaseMediaPlayer()
      }

    })

    val density = resources.displayMetrics.density
    val offset = (100 * density).toInt()
    binding.icon.setOnClickListener {
      drawImage()
      if (!hasClick) {
        blurImg()
        it.startAnimation(TranslateAnimation(0f, offset.toFloat(), 0f, -offset.toFloat()).apply {
          duration = DURATION
          fillAfter = true
        })
      }
      else {
        it.startAnimation(TranslateAnimation(offset.toFloat(), 0f, -offset.toFloat(), 0f).apply {
          duration = DURATION
          fillAfter = true
        })
      }
      hasClick = !hasClick

    }
    return binding.root
  }

  private fun applyBlur(blurRadius: Float) {
    lifecycleScope.launch(Dispatchers.Main) {
        binding.trans.alpha = blurRadius.coerceIn(0.2f,1f)
        binding.icon.alpha = 1-blurRadius.coerceIn(0f,0.4f)
    }
  }

  fun blurImg() {
    // 在协程中逐渐增加模糊效果
    val durationMillis = DURATION
    viewLifecycleOwner.lifecycleScope.launch {

      val startTime = System.currentTimeMillis()
      var elapsedTime = 100L

      while (elapsedTime < durationMillis) {
        val progress = elapsedTime.toFloat() / durationMillis
        val currentBlurRadius = (1f) * progress


        withContext(Dispatchers.Main) {
          "加载了 currentBlurRadius = ${currentBlurRadius}".logE()

          applyBlur(currentBlurRadius)
        }

        delay(5) // 每16毫秒更新一次模糊效果
        elapsedTime = System.currentTimeMillis() - startTime
        if(elapsedTime >= durationMillis){
          applyBlur(1f)
        }
      }
    }
  }

  var hasClick = false

  fun playVideo(videoName: String) {
    releaseMediaPlayer()

    val afd = requireContext().assets.openFd(videoName)
    val fd = afd.fileDescriptor

    mediaPlayer = MediaPlayer()
    mediaPlayer?.setDataSource(fd,afd.startOffset,afd.length)
    mediaPlayer?.setDisplay(binding.surface.holder)
    mediaPlayer?.prepareAsync()

    mediaPlayer?.setOnPreparedListener {
      mediaPlayer?.start()
    }
  }

  private fun releaseMediaPlayer() {
    mediaPlayer?.let {
      it.stop()
      it.release()
      mediaPlayer = null
    }
  }


}