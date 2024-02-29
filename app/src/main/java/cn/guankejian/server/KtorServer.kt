package cn.guankejian.server

import android.content.Context
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object KtorServer {

  lateinit var appContext: Context
  lateinit var pushDataCallBack: PushDataCallBack


  private val server by lazy {
    embeddedServer(Netty, 12345) {
      install(ContentNegotiation) // 可选，如果你需要处理JSON等内容

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

        handleStaticFileRequest()
        handleNormalRequests()



        handleFileUpload()

      }
    }
  }






  /** 启动服务器 */
  fun start(context: Context, pushDataCallBack: PushDataCallBack) {
    appContext = context
    CoroutineScope(Dispatchers.IO).launch { server.start(true) }
  }

  /** 停止服务器 */
  fun stop() {
    server.stop(1_000, 2_000)
  }
}