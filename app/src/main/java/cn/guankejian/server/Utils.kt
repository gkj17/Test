package cn.guankejian.server

import android.util.Log
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

/**
 * 内网IP地址
 */
val localIPAddress: String?
    get() {
        val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf: NetworkInterface = en.nextElement()
            val interfaceName = intf.displayName
            Log.d("ASD","interfaceName = ${interfaceName}")
            println("interfaceName = ${interfaceName}")
            if(interfaceName == "eth0" || interfaceName == "wlan0") {
                val enumIpAddr: Enumeration<InetAddress> = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress: InetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.hostAddress.toString()
                    }
                }
            }
        }
        return null
    }