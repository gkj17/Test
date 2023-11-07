package cn.guankejian.server

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.blankj.utilcode.util.Utils


class App : Application() {


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
  }
}