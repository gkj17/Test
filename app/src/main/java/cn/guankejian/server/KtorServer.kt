package cn.guankejian.server

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.core.content.res.ResourcesCompat
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader

object KtorServer {

    lateinit var appContext: Context


    private val server by lazy {
        embeddedServer(Netty, 12345) {
            install(CallLogging)
            // 跨域访问
            install(CORS) {
                anyHost()
                header(HttpHeaders.ContentType)
                method(HttpMethod.Options)
                method(HttpMethod.Put)
                method(HttpMethod.Patch)
                method(HttpMethod.Delete)
            }

            routing {
                get("/") {
                    call.respondText("手机k型号 ${Build.MODEL} 运行正常", ContentType.Text.Plain)
                }
                get("/index.html") {
                    call.respondText(readFileFromRawDirectory(R.raw.index), ContentType.Text.Html)
                }
                get("/style.css"){
                    call.respondText(readFileFromRawDirectory(R.raw.style), ContentType.Text.CSS)
                }
                get("/ui.css"){
                    call.respondText(readFileFromRawDirectory(R.raw.ui), ContentType.Text.CSS)
                }
                get("/jquery.js"){
                    call.respondText(readFileFromRawDirectory(R.raw.jquery), ContentType.Text.JavaScript)
                }
                get("/script.js"){
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
        }
    }
    private fun readFileFromRawDirectory(resourceId: Int): String {
        val inputStream = appContext.resources.openRawResource(resourceId)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val content = reader.readText()
        reader.close()
        return content
    }

    private fun getBytesFromDrawableResource(drawableId: Int): ByteArray {
        val drawable = appContext.resources.getDrawable(drawableId, null)
        val bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun getBitmapFromDrawableResource(drawableId: Int): Bitmap {
        val drawable = ResourcesCompat.getDrawable(appContext.resources,drawableId, null)
        return (drawable as BitmapDrawable).bitmap
    }

    private fun bitmapToBytes(bitmap: Bitmap, format: Bitmap.CompressFormat): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(format, 100, stream)
        return stream.toByteArray()
    }


    /** 启动服务器 */
    fun start(context:Context) {
        appContext = context
        CoroutineScope(Dispatchers.IO).launch { server.start(true) }
    }

    /** 停止服务器 */
    fun stop() {
        server.stop(1_000, 2_000)
    }
}