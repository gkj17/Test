package cn.guankejian.server.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import cn.guankejian.server.R
import cn.guankejian.server.databinding.FragmentFirstBinding
import cn.guankejian.server.localIPAddress


@ExperimentalPagingApi
class FragmentFirst constructor(
) : Fragment() {
  private lateinit var binding: FragmentFirstBinding
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

    binding.text.text = "请打开局域网中的任意浏览器访问 Http://${localIPAddress ?: "0.0.0.0"}:12345"



    return binding.root
  }




}