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
//    private const val BASE_URL = "http://119.23.65.122:8080/"

    fun <T> create(clazz: Class<T>, dynamicDns: DynamicDns): T {

      val logger = HttpLoggingInterceptor()
      logger.level = HttpLoggingInterceptor.Level.BODY

      val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .dns(dynamicDns) // 使用动态DNS
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