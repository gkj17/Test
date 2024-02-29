package cn.guankejian.server

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Dns
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import java.net.InetAddress
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

  public fun buildDnsOverHttps(provider: DnsProvider): Dns {
    val httpClient = OkHttpClient.Builder().build()
    return DnsOverHttps.Builder().client(httpClient)
      .url(provider.url.toHttpUrl())
      .bootstrapDnsHosts(
        //Cloudflare
          InetAddress.getByName("1.1.1.1"),
          InetAddress.getByName("1.0.0.1"),
        //阿里云
          InetAddress.getByName("223.5.5.5"),
          InetAddress.getByName("223.6.6.6"),
        //腾讯云
          InetAddress.getByName("119.29.29.29"),
          InetAddress.getByName("182.254.116.116"),
        //114
          InetAddress.getByName("114.114.114.114"),
          InetAddress.getByName("114.114.115.115"),
        //CNNIC SDNS:
          InetAddress.getByName("1.2.4.8"),
          InetAddress.getByName("210.2.4.8"),
        //OpenDNS
          InetAddress.getByName("208.67.222.222"),
          InetAddress.getByName("208.67.220.220"),
        //Quad9
          InetAddress.getByName("9.9.9.9"),
          InetAddress.getByName("149.112.112.112"),
        //Google
          InetAddress.getByName("8.8.8.8"),
          InetAddress.getByName("8.8.4.4"),
      )
      .build()
  }

  @Singleton
  @Provides
  fun provideDynamicDns(): DynamicDns {
    // 默认使用系统DNS
    val dynamicDns = DynamicDns()

    // 设置为你想要的默认DoH DNS，例如Google
    val googleDns = buildDnsOverHttps(DnsProvider.GOOGLE)
    dynamicDns.setDns(googleDns)

    return dynamicDns
  }


  @Singleton
  @Provides
  fun provideVipService(dynamicDns: DynamicDns): VipService {
    return BaseService.create(VipService::class.java, dynamicDns)
  }

}