package cn.guankejian.server.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.ExperimentalPagingApi
import cn.guankejian.server.DnsProvider
import cn.guankejian.server.DynamicDns
import cn.guankejian.server.NetworkModule
import cn.guankejian.server.R
import cn.guankejian.server.VipService
import cn.guankejian.server.databinding.FragmentFirstBinding
import cn.guankejian.server.databinding.TvMovieDetailFragmentBinding
import cn.guankejian.server.localIPAddress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalPagingApi
@AndroidEntryPoint
class FragmentFirst constructor(
) : Fragment() {
  private lateinit var binding: TvMovieDetailFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {


    binding = DataBindingUtil.inflate(
      LayoutInflater.from(requireContext()),
      R.layout.tv_movie_detail_fragment,
      container,
      false
    )

    return binding.root
  }




}