package cn.guankejian.server

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.receiveMultipart
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondBytes
import io.ktor.response.respondFile
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStreamReader

fun Routing.handleStaticFileRequest() {
  get("/") {
    call.respondText("大帅逼", ContentType.Text.Plain)
  }
  get("/index.html") {
    call.respondText(readFileFromRawDirectory(R.raw.index), ContentType.Text.Html)
  }
  get("/style.css") {
    call.respondText(readFileFromRawDirectory(R.raw.style), ContentType.Text.CSS)
  }
  get("/ui.css") {
    call.respondText(readFileFromRawDirectory(R.raw.ui), ContentType.Text.CSS)
  }
  get("/jquery.js") {
    call.respondText(readFileFromRawDirectory(R.raw.jquery), ContentType.Text.JavaScript)
  }
  get("/script.js") {
    call.respondText(readFileFromRawDirectory(R.raw.script), ContentType.Text.JavaScript)
  }
  get("/favicon.ico") {
    val iconBitmap = getBitmapFromDrawableResource(R.drawable.app_icon)
    val iconBytes = bitmapToBytes(iconBitmap, Bitmap.CompressFormat.PNG)
    call.respondBytes(iconBytes, ContentType.Image.PNG)

//                    val iconBytes = getBytesFromDrawableResource(R.drawable.app_icon)
//                    call.respondBytes(iconBytes, ContentType.Image.PNG)
  }
}
fun Routing.handleFileUpload() {
  post("/upload") {
    val multipart = call.receiveMultipart()
    var path = ""
    multipart.forEachPart { part ->
      when (part) {
        is PartData.FormItem -> {
          if (part.name == "path") {
            path = part.value
          }
        }
        is PartData.FileItem -> {
          val fileName = part.originalFileName as String
          val fileBytes = part.streamProvider().readBytes()
          File("uploads/$path/$fileName").apply {
            parentFile.mkdirs()
            writeBytes(fileBytes)
          }
        }

        is PartData.BinaryItem -> {

        }
      }
      part.dispose()
    }
    call.respondText("File uploaded to 'uploads/$path'", ContentType.Text.Plain)
  }


  post("/newFolder") {
    val params = call.receiveParameters()
    val path = params["path"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Path parameter is missing")
    val name = params["name"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Name parameter is missing")
    val folder = File("uploads/$path/$name").apply {
      if (!exists()) {
        mkdirs()
        File(this, ".tvbox_folder").createNewFile() // 标记文件夹
      }
    }
    call.respondText("New folder created at '${folder.absolutePath}'", ContentType.Text.Plain)
  }

  post("/delFolder") {
    val params = call.receiveParameters()
    val path = params["path"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Path parameter is missing")
    val folder = File("uploads/$path")
    if (folder.exists()) {
      folder.deleteRecursively()
      call.respondText("Folder deleted successfully", ContentType.Text.Plain)
    } else {
      call.respond(HttpStatusCode.NotFound, "Folder not found")
    }
  }

  post("/delFile") {
    val params = call.receiveParameters()
    val path = params["path"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Path parameter is missing")
    val file = File("uploads/$path")
    if (file.exists()) {
      file.delete()
      call.respondText("File deleted successfully", ContentType.Text.Plain)
    } else {
      call.respond(HttpStatusCode.NotFound, "File not found")
    }
  }
}
fun Routing.handleNormalRequests() {
  post("/action") {
    val parameters = call.receiveParameters()
    val action = parameters["do"]
    //通过paramters[value]去获取值
    Log.d("ASD","parameters = ${parameters}")
    val value = parameters["value"]
    when (action) {
      //添加一些回调
      "search" -> {
        KtorServer.pushDataCallBack.onSearchKeyWordChanged(value)
      }
      "push" -> {
        KtorServer.pushDataCallBack.onPushUrlChanged(value)
      }
      "source" -> {
        KtorServer.pushDataCallBack.onSourceUrlChanged(value)
      }
      "live" -> {
        KtorServer.pushDataCallBack.onLiveUrlChanged(value)
      }
      "epg" -> {
        KtorServer.pushDataCallBack.onEpgUrlChanged(value)
      }
      else ->
        Log.d("ASD", "action = ${action}")
      //"Unsupported action"
    }
  }

  get("/file/{fileName}"){
    val fileName = call.parameters.getAll("fileName")?.joinToString("/") ?: return@get call.respond(
      HttpStatusCode.NotFound)
    Log.d("ASD","fileName = ${fileName}")
    val root = Environment.getExternalStorageDirectory().absolutePath

    val file = File("root/$fileName")
    if (file.exists()) {
      call.respondFile(file)
    } else {
      call.respond(HttpStatusCode.NotFound, "File not found.")
    }
  }
  get("/dns-query"){
    val name = call.request.queryParameters["name"]
    if (name != null) {
      // TODO:
    } else {
      call.respond(HttpStatusCode.BadRequest, "Query parameter 'name' is missing.")
    }
  }
  get("/m3u8") {
    // 假设m3u8Content是预先定义好的M3U8内容
    call.respondText("m3u8 content is not implemented.", ContentType.Text.Plain)
  }
  get("/dash/{data...}") {
    val data = call.parameters.getAll("data")?.joinToString("/") ?: return@get call.respond(
      HttpStatusCode.NotFound)
    // 假设dashData是预先定义好的DASH数据
    call.respondText("DASH data for $data is not implemented.", ContentType.Application.Xml)
  }
}

private fun readFileFromRawDirectory(resourceId: Int): String {
  val inputStream = KtorServer.appContext.resources.openRawResource(resourceId)
  val reader = BufferedReader(InputStreamReader(inputStream))
  val content = reader.readText()
  reader.close()
  return content
}

private fun getBytesFromDrawableResource(drawableId: Int): ByteArray {
  val drawable = ResourcesCompat.getDrawable(KtorServer.appContext.resources, drawableId, null)
  val bitmap = (drawable as BitmapDrawable).bitmap
  val stream = ByteArrayOutputStream()
  bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
  return stream.toByteArray()
}

private fun getBitmapFromDrawableResource(drawableId: Int): Bitmap {
  val drawable = ResourcesCompat.getDrawable(KtorServer.appContext.resources, drawableId, null)
  return (drawable as BitmapDrawable).bitmap
}

private fun bitmapToBytes(bitmap: Bitmap, format: Bitmap.CompressFormat): ByteArray {
  val stream = ByteArrayOutputStream()
  bitmap.compress(format, 100, stream)
  return stream.toByteArray()
}