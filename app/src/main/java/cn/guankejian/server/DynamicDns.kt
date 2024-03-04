package cn.guankejian.server

import android.util.Log
import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import java.net.InetAddress
import java.net.UnknownHostException

class DynamicDns(private val defaultDns: Dns = Dns.SYSTEM) : Dns {
    @Volatile
    private var dns: Dns = defaultDns

    @Synchronized
    fun setDns(newDns: Dns) {

        dns = newDns
    }
    fun isIPv4Address(input: String): Boolean {
        val ipv4Regex = Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")
        return ipv4Regex.matches(input)
    }

    fun isIPv6Address(input: String): Boolean {
        val ipv6Regex = Regex("^([0-9a-fA-F]{1,4}:){7}([0-9a-fA-F]{1,4})$")
        return ipv6Regex.matches(input)
    }

    fun isIPAddress(input: String): Boolean {
        return isIPv4Address(input) || isIPv6Address(input)
    }

    override fun lookup(hostname: String): List<InetAddress> {
        if (isIPAddress(hostname)) {
            return listOf(InetAddress.getByName(hostname))
        }
        val addresses = try{
            dns.lookup(hostname)
        }catch (e: UnknownHostException){
            "域名解析出错，使用系统DNS解析：\n${e.stackTraceToString()}".logE()
            Dns.SYSTEM.lookup(hostname)
        }
        Log.d("ASD", "解析 $hostname: ${addresses.joinToString(", ")}")
        return addresses
    }



}
