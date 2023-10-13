package cn.guankejian.test.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import cn.guankejian.test.App
import cn.guankejian.test.ComplexWindow
import cn.guankejian.test.LanJingMovingDraggable
import cn.guankejian.test.R
import cn.guankejian.test.databinding.FragmentFirstBinding
import cn.guankejian.test.databinding.WindowLanjingBinding


@ExperimentalPagingApi
class FragmentFirst constructor(
) : Fragment() {
  private lateinit var binding: FragmentFirstBinding

  lateinit var windowViewBinding: WindowLanjingBinding
  fun initWindowViewBinding() {
    windowViewBinding = DataBindingUtil.inflate<WindowLanjingBinding>(
      LayoutInflater.from(requireActivity().applicationContext),
      R.layout.window_lanjing,
      null,
      false
    )
//    windowViewBinding.coordinator.addView(client.view)
  }

  companion object{
    var WINDOW_WIDTH = 800
    var WINDOW_HEIGH = 800
    var GAP = 50
    var STEP = 0
  }
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    binding = DataBindingUtil.inflate(
      LayoutInflater.from(requireContext()),
      R.layout.fragment_first,
      container,
      false
    )


    val windowWidth = WINDOW_WIDTH - STEP
    val windowHeigh = WINDOW_HEIGH - STEP

    initWindowViewBinding()
    val windows = ComplexWindow.with(App.getInstance())
      .setContentView(windowViewBinding.root)

      windows
      .setGravity(Gravity.END)
      .setWidth(windowWidth)
      .setHeight(windowHeigh)
      .setDraggable(LanJingMovingDraggable(
        (windowWidth).toFloat(),
        (windowHeigh).toFloat(),
        (windowWidth - GAP).toFloat(),
        (windowHeigh - GAP).toFloat()
      ))
      .show()

    binding.hidden.setOnClickListener {
      windows.pause()
    }

    binding.show.setOnClickListener {
      windows.resume()
    }


    binding.test.setOnClickListener {
      Toast.makeText(requireContext(), "asd", Toast.LENGTH_SHORT).show()
    }
    return binding.root
  }



}