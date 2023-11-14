package cn.guankejian.server

import kotlin.concurrent.thread
import kotlin.random.Random

class ClientServer{
  fun interface Callback{
    fun notifyEvent()
  }

  var callback:Callback?=null
  fun setCallBack(callback:Callback){
    this.callback = callback
  }
  fun action(){
    thread(true) {
      println("viceVpa【开始】跳出")
      Thread.sleep(Random.nextInt(500, 1000).toLong())//模拟未知执行时间
      System.err.println("viceVpa【完成】跳出")
      callback?.notifyEvent()
    }
  }
}