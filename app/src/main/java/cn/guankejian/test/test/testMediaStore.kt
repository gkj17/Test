package cn.guankejian.test.test

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import cn.guankejian.test.logD
import cn.guankejian.test.logE
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

var compareApp:Boolean = false
var targetSdkVersion:Int=0
lateinit var testFileName :String;
lateinit var mediaStoreName:String;


fun testMediaStore(context: Context) {
    targetSdkVersion = context.applicationInfo.targetSdkVersion
    testFileName = "testFile" + targetSdkVersion
    mediaStoreName = "main" + targetSdkVersion
    compareApp = context.packageName == "cn.guankejian.test2"
    "!!!!     !!!!!!!!!!!!!!!!!!     !!!!!!!!!     !!!!!!!!!!     !!!!!!!     !!!!!!!!!!!!!!!!!!!!!     !!!!!!!!!!!!!!!!!!!!!!     !!!!!".logE()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        "分区存储【${if (Environment.isExternalStorageLegacy()) "已" else "还没"}】开启"
    }
    if(compareApp)"当前是测试app".logE()
    val resolver = context.applicationContext.contentResolver


    if(compareApp) {
        try {
            deleteFile(resolver)
        } catch (e: Exception) {
            "文件删除出错：${e.message}".logE()
            e.printStackTrace()
        }
    }

//    try {
//        queryImage(resolver)
//    } catch (e: Exception) {
//        "图片查询出错：${e.message}".logE()
//        e.printStackTrace()
//    }

    try {
        queryFile(resolver)
    } catch (e: Exception) {
        "文件查询出错：${e.message}".logE()
        e.printStackTrace()
    }



    try {
        testJicheng(resolver,"音频")
    } catch (e: Exception) {
        "音频出错：${e.message}".logE()
        e.printStackTrace()
    }
    try {
        testJicheng(resolver,"视频")
    } catch (e: Exception) {
        "视频出错：${e.message}".logE()
        e.printStackTrace()
    }
    try {
        testJicheng(resolver,"图片")
    } catch (e: Exception) {
        "图片出错：${e.message}".logE()
        e.printStackTrace()
    }
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            testJicheng(resolver,"下载")
        }
    } catch (e: Exception) {
        "下载出错：${e.message}".logE()
        e.printStackTrace()
    }
    try {
        queryFile(resolver)
    } catch (e: Exception) {
        "文件查询出错：${e.message}".logE()
        e.printStackTrace()
    }

//    try {
//        queryImage(resolver)
//    } catch (e: Exception) {
//        "图片查询出错：${e.message}".logE()
//        e.printStackTrace()
//    }

//    try {
//        queryImage(resolver)
//    } catch (e: Exception) {
//        "图片查询出错：${e.message}".logE()
//        e.printStackTrace()
//    }

}


fun getName() = if (compareApp) testFileName else mediaStoreName


fun testJicheng(resolver: ContentResolver,name:String) {
    val collection:Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        when(name){
            "音频"->{
                MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            }
            "视频"->{
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            }
            "图片"->{
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            }
            else->{
                MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            }
        }
    }else{
        when(name){
            "音频"->{
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            "视频"->{
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }
            "图片"->{
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            else->{
                throw Exception("找不到")
            }
        }
    }

    val DISPLAY_NAME = when(name){
        "音频"->{
            MediaStore.Audio.Media.DISPLAY_NAME
        }
        "视频"->{
            MediaStore.Video.Media.DISPLAY_NAME
        }
        "图片"->{
            MediaStore.Images.Media.DISPLAY_NAME
        }
        else->{
            MediaStore.Downloads.DISPLAY_NAME
        }
    }

    val DATA = when(name){
        "音频"->{
            MediaStore.Audio.Media.DATA
        }
        "视频"->{
            MediaStore.Video.Media.DATA
        }
        "图片"->{
            MediaStore.Images.Media.DATA
        }
        else->{
            MediaStore.Downloads.DATA
        }
    }

    val ID = when(name){
        "音频"->{
            MediaStore.Audio.Media._ID
        }
        "视频"->{
            MediaStore.Video.Media._ID
        }
        "图片"->{
            MediaStore.Images.Media._ID
        }
        else->{
            MediaStore.Downloads._ID
        }
    }


    val subPrefix = when(name){
        "音频"->{
            ".mp3"
        }
        "视频"->{
            ".mp4"
        }
        "图片"->{
            ".jpg"
        }
        else->{
            ".pdf"
        }
    }


    val contentValues = ContentValues().apply {
        put(DISPLAY_NAME, "${getName()}${subPrefix}")
    }

    val uri = resolver
        .insert(collection, contentValues) ?: throw Exception("${name} uri is null")
    "【创建】【自己】的【${name}】成功 uri = ${uri}\tmimeType = ${resolver.getType(uri)}".logE()
    resolver.openOutputStream(uri)?.use {
        val writer = BufferedWriter(OutputStreamWriter(it))
        writer.write((if (compareApp) "compare" else "1").repeat(2))
        writer.close()
        "【写】【自己】【${name}】成功".logE()
    }


    resolver.query(uri,null,null,null)?.use {
        val dataColumnIndex = it.getColumnIndexOrThrow(DATA)
        val displayNameColumnIndex = it.getColumnIndexOrThrow(DISPLAY_NAME)
        if(it.moveToNext()) {
            val data = it.getString(dataColumnIndex)
            val displayName = it.getString(displayNameColumnIndex)
            "【读】【自己】【${name}】成功，名称是${displayName} 位置是：${data}  ".logD()
            resolver.openInputStream(uri)?.use {
                val reader = BufferedReader(InputStreamReader(it))
                "内容为：【${reader.readText()}】".logD()
            }
        }
    }




    if (!compareApp) {
                val contentValues2 = ContentValues().apply {
                    put(DISPLAY_NAME, "delete${subPrefix}")
                }

                val uri2 = resolver
                    .insert(collection, contentValues2) ?: throw Exception("${name} uri2 is null")
                "【创建】需要【删除】的${name}成功 uri = ${uri2}\tmimeType = ${resolver.getType(uri2)}".logD()
                resolver.query(uri,null,null,null)?.use {
                    val dataColumnIndex = it.getColumnIndexOrThrow(DATA)
                    val displayNameColumnIndex = it.getColumnIndexOrThrow(DISPLAY_NAME)
                    if(it.moveToNext()) {
                        val data = it.getString(dataColumnIndex)
                        val displayName = it.getString(displayNameColumnIndex)
                        "【读】需要【删除】的【${name}】成功，位置是：${data}  名称是${displayName}".logD()
                    }
                }
                resolver.openOutputStream(uri2)?.use {
                    val writer = BufferedWriter(OutputStreamWriter(it))
                    writer.write("1".repeat(2))
                    writer.close()
                    "【写】需要【删除】的${name}成功".logD()
                }

                if (resolver.delete(uri2, null, null) > 0) {
                    "【删】【自己】【${name}】成功".logE()
                } else {
                    "【删】【自己】【${name}】失败".logE()
                }


        resolver.query(collection, null, null, null)?.use {
            val idColumn = it.getColumnIndexOrThrow(ID)
            val nameColumnIndex = it.getColumnIndexOrThrow(DISPLAY_NAME)
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                if (it.getString(nameColumnIndex).contains(testFileName)) {
                    Uri.withAppendedPath(collection, id.toString()).let {
                        resolver.openInputStream(it)?.use {
                            val reader = BufferedReader(InputStreamReader(it))
                            "【读】【他人】【$name】成功,内容是：${reader.readText()}".logE()
                        }
                        resolver.openOutputStream(it)?.use {
                            val writer = BufferedWriter(OutputStreamWriter(it))
                            writer.write("input".repeat(2))
                            writer.close()
                            "【写】【他人】【$name】成功".logE()
                        }

                        resolver.openInputStream(it)?.use {
                            val reader = BufferedReader(InputStreamReader(it))
                            "【写】后【读】【他人】【$name】成功,内容是：${reader.readText()}".logE()
                        }

                        if (resolver.delete(it, null, null) > 0) {
                            "【删】【他人】【$name】成功".logE()
                        }
                    }
                }
            }
        }
    }
}

data class FileBean(
    val id: Long,
    val data: String,
    val mimeType: String?,
    val name: String?,
)

fun deleteFile(resolver: ContentResolver) {
    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Files.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )
        } else {
            MediaStore.Files.getContentUri("external")
        }

    var count = 0
//    val list = ArrayList<FileBean>()

    resolver.query(
        collection,
        null,
        null,
        null,
        null
    )?.use { cursor ->
        val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
        val dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
        val mimeTypeColumnIndex =
            cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
        val nameColumnIndex =
            cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumnIndex)
            val data = cursor.getString(dataColumnIndex)
            val mimeType = cursor.getString(mimeTypeColumnIndex)
            val name = cursor.getString(nameColumnIndex)


            val fileUri = ContentUris.withAppendedId(collection, id)

            if(resolver.delete(fileUri, null, null) > 0){
                "【删除】【成功】 data=${data}, mimeType=${mimeType}, name=${name}".logE()
                ++count;
            }else{
                "【删除】【失败】 data=${data}, mimeType=${mimeType}, name=${name}".logE()
            }
//            list.add(FileBean(id, data, mimeType, name))

        }
    }

    "成功删除了$count 个文件".logE()

}

fun queryFile(resolver: ContentResolver) {
    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Files.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )
        } else {
            MediaStore.Files.getContentUri("external")
        }

    val list = ArrayList<FileBean>()

    resolver.query(collection, null, null, null)?.use { cursor ->
        val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
        val dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
        val mimeTypeColumnIndex =
            cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
        val nameColumnIndex =
            cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumnIndex)
            val data = cursor.getString(dataColumnIndex)
            val mimeType = cursor.getString(mimeTypeColumnIndex) ?: continue
            val name = cursor.getString(nameColumnIndex) ?: continue
            list.add(FileBean(id, data, mimeType, name))
        }
    }

    "文件读取成功，读取的内容".logE()


    for (fileBean in list) {
        val type = if (fileBean.mimeType != null) {
            if (fileBean.mimeType.contains("audio")) {
                "音频"
            } else if (fileBean.mimeType.contains("video")) {
                "视频"
            } else if (fileBean.mimeType.contains("image")) {
                "图片"
            } else if (fileBean.mimeType.contains("application")) {
                "文件"
            } else ""
        } else {
            "null"
        }
        "查询到了 类型是【${type}】\tdata=${fileBean.data}, mimeType=${fileBean.mimeType}, name=${fileBean.name}".logE()

    }
}

fun queryImage(resolver: ContentResolver) {
    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

    val list = ArrayList<FileBean>()

    resolver.query(collection, null, null, null)?.use { cursor ->
        val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val mimeTypeColumnIndex =
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
        val nameColumnIndex =
            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumnIndex)
            val data = cursor.getString(dataColumnIndex)
            val mimeType = cursor.getString(mimeTypeColumnIndex) ?: continue
            val name = cursor.getString(nameColumnIndex) ?: continue
            list.add(FileBean(id, data, mimeType, name))
        }
    }

    "图片读取成功，读取的内容".logE()

    for (fileBean in list) {
        "$fileBean".logE()
    }
}


