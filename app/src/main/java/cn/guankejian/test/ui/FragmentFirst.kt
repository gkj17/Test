package cn.guankejian.test.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import cn.guankejian.test.R
import cn.guankejian.test.StorageUtil
import cn.guankejian.test.databinding.FragmentFirstBinding


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



    val a = StorageUtil.readPublicStorage(requireContext())
    a
    return binding.root
  }



}