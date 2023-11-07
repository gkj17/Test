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
import cn.guankejian.server.second_part.bean.Person


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



    return binding.root
  }




}