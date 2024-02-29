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
import cn.guankejian.server.localIPAddress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalPagingApi
@AndroidEntryPoint
class FragmentFirst constructor(
) : Fragment() {
  private lateinit var binding: FragmentFirstBinding

  @Inject
  lateinit var dynamicDns: DynamicDns

  fun switchDns(provider: DnsProvider) {
    val newDns = NetworkModule().buildDnsOverHttps(provider)
    dynamicDns.setDns(newDns)
  }





  @Inject
  lateinit var vipService: VipService

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


    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        delay(2000)
        vipService.index().toString().let {
          Log.d("ASD", it)
          binding.text.text = it
        }
      }
    }
    val dnslist = listOf(
      DnsProvider.DOT_PUB,
      DnsProvider.ALI_DNS,
      DnsProvider.QIHU_360,
      DnsProvider.GOOGLE,
      DnsProvider.ADGUARD,
      DnsProvider.QUAD9
    )
    binding.button.setOnClickListener {
      switchDns(dnslist.random())
    }


    return binding.root
  }




}