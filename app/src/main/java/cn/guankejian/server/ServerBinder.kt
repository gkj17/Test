package cn.guankejian.server

import android.os.Message
import android.os.Parcel
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.util.Log
import coil.ImageLoader
import com.blankj.utilcode.util.LogUtils
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock


class ServerBinder() : IServerListener.Stub() {
  private val receiveListeners = RemoteCallbackList<IClientListener>()
  private val _lock: Unit = Unit

  override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
    return try {
      super.onTransact(code, data, reply, flags)
    } catch (e: RuntimeException) {
      e.printStackTrace()
      throw e
    }
  }

  override fun client2Server(key: String, value: String) {
    "".logD()
    if(key == "client_a")
      Thread.sleep(5)
    try {
      "收到客户端${key}发送的数据----：$value".logD()
        App.getInstance().handler.sendMessage(Message.obtain().apply {
          what = 1
          obj = Pair(key, value)
        })
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }


  fun sendMessageToClient(clientId: String, value: String) {
    synchronized(_lock){
      try {
        "sendMessageToClient -> currentThread = ${Thread.currentThread()}".logD("receiveListeners = ${receiveListeners}")

        val count = receiveListeners.beginBroadcast()
        "sendMessageToClient -> count = ${count}".logD()
        for (i in 0 until count) {
          val item = receiveListeners.getBroadcastItem(i)
          if (item.clientId == clientId) {
            item.server2client("server", value)
            break;
          }
        }
      } catch (e: RemoteException) {
        e.printStackTrace()
      } finally {
        receiveListeners.finishBroadcast()
      }
    }
  }

  override fun registerListener(listener: IClientListener) {
    "registerListener -> listener=${listener}".logD()
    "sendMessageToClient -> currentThread = ${Thread.currentThread()}".logD("receiveListeners = ${receiveListeners}")


    receiveListeners.register(listener)
  }

  override fun unregisterListener(listener: IClientListener) {
    val success = receiveListeners.unregister(listener) ?: false
    if (success) {
      "解除注册成功".logD()
    } else {
      "解除注册失败".logD()
    }
  }
}