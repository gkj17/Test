package cn.guankejian.server

import org.junit.Test

//通过加锁实现
class WaitNotifyTest {

  val lock = Object()

  //地球上的Vpa
  fun viceVpaAcionByAidl() {
    val client = ClientServer()
    client.setCallBack {
      synchronized(lock) {
        lock.notify()
      }
    }
    client.action()
  }

  fun vpaAction() {
    println("Vpa【开始】跳出")
    Thread.sleep(500)
    System.err.println("Vpa【完成】跳出")
  }

  @Test
  fun testCoroutines() {
    viceVpaAcionByAidl()
    synchronized(lock) {
      lock.wait()
    }
    vpaAction()
    Thread.sleep(100)
  }
}