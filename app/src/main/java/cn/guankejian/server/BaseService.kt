package cn.guankejian.server

import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.Arrays


interface BaseService {

  companion object {
    private const val BASE_URL = "https://node.video.qq.com/"

    fun <T> create(clazz: Class<T>, dynamicDns: DynamicDns): T {

      val logger = HttpLoggingInterceptor()
      logger.level = HttpLoggingInterceptor.Level.BODY

      val dnsOverHttps = DnsOverHttps.Builder()
        .client(OkHttpClient.Builder().build())
        .url("https://dns.alidns.com/dns-query".toHttpUrl()) // 使用Kotlin扩展函数
        .bootstrapDnsHosts(
          listOf(
            InetAddress.getByName("223.5.5.5"),
            InetAddress.getByName("223.6.6.6"),
          )
        )
        .build()

      val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .dns(dnsOverHttps) // 使用动态DNS
        .build()

      return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(clazz)
    }
  }

}