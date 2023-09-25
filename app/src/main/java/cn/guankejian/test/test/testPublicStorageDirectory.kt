package cn.guankejian.test.test

import android.content.Context
import android.os.Build
import android.os.Environment
import cn.guankejian.test.logE
import java.io.File
import java.io.FileWriter

val createFileName = "test70.png"
fun testPublicStorageDirectory(context:Context) {
    try {
        readBefore(context)
    } catch (e: Exception) {
        "读【自己】失败->${e}".logE()
    }
    try {
        write(context)
    } catch (e: Exception) {
        "创建【自己】失败->${e}".logE()
    }
    try {
        writeContent(context)
    } catch (e: Exception) {
        "写【自己】失败->${e}".logE()
    }
    try {
        writeOther(context)
    } catch (e: Exception) {
        "写【他人】失败->${e}".logE()
    }
    try {
        readAfter(context)
    } catch (e: Exception) {
        "读【自己】失败->${e}".logE()
    }
}

fun readBefore(context:Context) {
    val downloadFile =
        File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/app_icon.png")
    val dcimFile =
        File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/app_icon.png")
    downloadFile.length().also {
        if (it > 0)
            "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}读【他人】的【Download】文件【成功】".logE()
        else
            "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}读【他人】的【Download】文件【失败】".logE()
    }
    dcimFile.length().also {
        if (it > 0)
            "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}读【他人】的【DCIM】文件【成功】".logE()
        else
            "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}读【他人】的【DCIM】文件【失败】".logE()
    }

}

fun write(context:Context) {
    val downloadFile =
        File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/$createFileName")
    if (downloadFile.exists()) {
        downloadFile.delete()
    }

    val dcimFile =
        File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/$createFileName")
    if (dcimFile.exists()) {
        dcimFile.delete()
    }


    if (downloadFile.createNewFile())
        "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}创建【自己】的【Download】文件【成功】".logE()
    else
        "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}创建【自己】的【Download】文件【失败】".logE()
    if (dcimFile.createNewFile())
        "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}创建【自己】的【DCIM】文件【成功】".logE()
    else
        "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}创建【自己】的【DCIM】文件【失败】".logE()
}


fun writeContent(context:Context) {
    val downloadFile =
        File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/$createFileName")
    FileWriter(downloadFile).use {
        it.write("downloadFile")
    }

    val dcimFile =
        File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/$createFileName")

    FileWriter(dcimFile).use {
        it.write("dcimFile")
    }

    if (downloadFile.length() > 0)
        "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}写【自己】的【Download】文件【成功】".logE()
    else
        "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}写【自己】的【Download】文件【失败】".logE()
    if (dcimFile.length() > 0)
        "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}写【自己】的【DCIM】文件【成功】".logE()
    else
        "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}写【自己】的【DCIM】文件【失败】".logE()
}


fun writeOther(context:Context) {
    val downloadFile =
        File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/app_icon.png")
    FileWriter(downloadFile, true).use {
        it.write(createFileName)
    }

    val dcimFile =
        File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/app_icon.png")

    FileWriter(dcimFile, true).use {
        it.write(createFileName)
    }

    if (downloadFile.readText().contains(createFileName))
        "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}写【他人】的【Download】文件【成功】".logE()
    else
        "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}写【他人】的【Download】文件【失败】".logE()
    if (dcimFile.readText().contains(createFileName))
        "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}写【他人】的【DCIM】文件【成功】".logE()
    else
        "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}写【他人】的【DCIM】文件【失败】".logE()
}

fun readAfter(context:Context) {
    val downloadFile =
        File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/$createFileName")
    val dcimFile =
        File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/$createFileName")
    downloadFile.length().also {
        if (it > 0)
            "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}读【自己】的【Download】文件【成功】".logE()
        else
            "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}读【自己】的【Download】文件【失败】".logE()
    }
    dcimFile.length().also {
        if (it > 0)
            "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}读【自己】的【DCIM】文件【成功】".logE()
        else
            "targetSdk = ${context.applicationInfo.targetSdkVersion}\t BuildSdk = ${Build.VERSION.SDK_INT}读【自己】的【DCIM】文件【失败】".logE()
    }
}