package cn.guankejian.server

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService

class ServerService: LifecycleService() {

  val binder = ServerBinder()
  override fun onBind(intent: Intent): IBinder {
    super.onBind(intent)
    return binder
  }
}