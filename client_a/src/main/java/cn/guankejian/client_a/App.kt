package cn.guankejian.client_a

import android.app.Application


class App : Application() {
  companion object{

    private var application:Application?=null


    @JvmStatic
    fun getInstance():Application{
      return application!!
    }
  }
  override fun onCreate() {
    super.onCreate()
    application = this
  }
}