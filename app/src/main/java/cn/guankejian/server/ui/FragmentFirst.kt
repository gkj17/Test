package cn.guankejian.server.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.ExperimentalPagingApi
import cn.guankejian.server.BaseService
import cn.guankejian.server.DnsProvider
import cn.guankejian.server.DynamicDns
import cn.guankejian.server.R
import cn.guankejian.server.VipService
import cn.guankejian.server.databinding.FragmentFirstBinding
import cn.guankejian.server.localIPAddress
import cn.guankejian.server.logE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import java.net.InetAddress


@ExperimentalPagingApi
@AndroidEntryPoint
class FragmentFirst constructor(
) : Fragment() {
  private lateinit var binding: FragmentFirstBinding


  var dynamicDns: DynamicDns = DynamicDns()

  lateinit var vipService: VipService


  init {
    updateOkHttpClient(DnsProvider.ALI_DNS) // 初始设置
  }
  fun updateOkHttpClient(provider: DnsProvider) {
    "provider = ${provider}".logE()
    val dohDns = buildDnsOverHttps(provider)
    dynamicDns.setDns(dohDns)
    vipService = BaseService.create(VipService::class.java, dynamicDns)
  }

  fun buildDnsOverHttps(provider: DnsProvider): Dns {
    val httpClient = OkHttpClient.Builder().build()
    return DnsOverHttps.Builder().client(httpClient)
      .url(provider.url.toHttpUrl())
      .bootstrapDnsHosts(getBootStrapDnsHost(provider))
      .build()
  }


  fun getBootStrapDnsHost(provider: DnsProvider): List<InetAddress> {
    val list = mutableListOf<String>()
    when (provider) {
      DnsProvider.DOT_PUB -> {
        list.add("1.12.12.12")
        list.add("120.53.53.53")
      }
      DnsProvider.ALI_DNS -> {
        list.add("223.5.5.5")
        list.add("223.6.6.6")
      }
      DnsProvider.QIHU_360 -> {
        list.add("101.198.191.4")
      }
      DnsProvider.GOOGLE -> {
        list.add("8.8.8.8")
        list.add("8.8.4.4")
      }
      DnsProvider.ADGUARD -> {
        list.add("94.140.15.15")
        list.add("94.140.14.14")
      }
      DnsProvider.QUAD9 -> {
        list.add("9.9.9.9")
        list.add("149.112.112.112")
      }
      DnsProvider.CLOUDFLARE -> {
        list.add("1.1.1.1")
      }
      DnsProvider.MOZILLA -> {
        list.add("172.64.41.4")
        list.add("162.159.61.4")
      }
      DnsProvider.SWITCH -> {
        list.add("103.2.57.6")
      }
      DnsProvider.NEXTDNS->{
        list.add("45.11.104.186")
        list.add("37.252.249.233")
      }
      DnsProvider.OPEN_DNS->{
        list.add("208.67.220.220")
        list.add("208.67.222.222")
      }

      DnsProvider.LIBREDNS -> {
        list.add("116.202.176.26")
      }
      DnsProvider.COMCAST -> {
        list.add("75.75.77.99")
      }
      DnsProvider.CLEANBROWSING -> {
        list.add("185.228.168.10")
        list.add("185.228.168.168")
      }
    }
    return list.map{
      InetAddress.getByName(it)
    }
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

    binding.text.text = "请打开局域网中的任意浏览器访问 Http://${localIPAddress ?: "0.0.0.0"}:12345"


    test()

    binding.button.setOnClickListener {
      binding.text.text = "请打开局域网中的任意浏览器访问 Http://${localIPAddress ?: "0.0.0.0"}:12345"

      updateOkHttpClient(DnsProvider.DOT_PUB) // 初始设置
      test()
    }


    return binding.root
  }
  val flag = false
  fun test(){
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

        if(flag) {
          vipService.index2().items.joinToString("\n").let {
            Log.d("ASD", it)
            binding.text.text = it
          }
        }else{
          vipService.index().toString().let {
            Log.d("ASD", it)
            binding.text.text = it
          }
        }


      }
    }
  }




}