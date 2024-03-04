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

	@Singleton
	@Provides
	fun provideVipService(dynamicDns: DynamicDns): VipService {
		return BaseService.create(VipService::class.java, dynamicDns)
	}

}