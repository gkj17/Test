package cn.guankejian.server

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.blankj.utilcode.util.Utils


class App : Application() {

  /**
   * 从客户端接收到数据回调
   */
  private var getDataCallback: OnGetClientDataCallback? = null
  /**
   * 从服务端向客户端发送数据回调
   */
  private var sendDataCallback: OnSendClientDataCallback? = null

  fun setOnGetClientDataCallback(callback: OnGetClientDataCallback?) {
    this.getDataCallback = callback
  }

  fun setOnSendClientDataCallback(callback: OnSendClientDataCallback?) {
    this.sendDataCallback = callback
  }

  interface OnGetClientDataCallback {
    fun onGetClientData(key: String, value:String)
  }

  interface OnSendClientDataCallback {
    fun onSendClientData(key: String, value:String)
  }
  val handler: Handler = object : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
      when(msg.what){
        1->{
          val pair = msg.obj as Pair<String,String>
          getDataCallback?.onGetClientData(pair.first,pair.second)
        }
        2->{
          val pair=msg.obj as Pair<String,String>
          sendDataCallback?.onSendClientData(pair.first,pair.second)
        }
      }
    }
  }

  companion object{

    private var application:App?=null



    @JvmStatic
    fun getInstance():App{
      return application!!
    }
  }
  override fun onCreate() {
    super.onCreate()
    application = this

    Utils.init(this)

//    val intent = Intent(application, ServerService::class.java)
//    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//    applicationContext.startForegroundService(intent)
  }
}