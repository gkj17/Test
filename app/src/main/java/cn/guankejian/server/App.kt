package cn.guankejian.server

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
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

  }
}