package cn.guankejian.server

import android.util.Log
import okhttp3.Dns
import java.net.InetAddress

class DynamicDns(private val defaultDns: Dns = Dns.SYSTEM) : Dns {
    @Volatile
    private var dns: Dns = defaultDns

    @Synchronized
    fun setDns(newDns: Dns) {
        Log.d("ASD", "切换DNS到: ${newDns.javaClass.simpleName}")

        dns = newDns
    }

    override fun lookup(hostname: String): List<InetAddress> {
        val addresses = dns.lookup(hostname)
        Log.d("ASD", "解析 $hostname: ${addresses.joinToString(", ")}")
        return addresses
    }
}
