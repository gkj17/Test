package cn.guankejian.server

import org.junit.Test

//通过加锁实现
class PollingTest {

  var isFinish: Boolean = false

  //地球上的Vpa
  fun viceVpaAcionByAidl() {
    val client = ClientServer()
    client.setCallBack{
      isFinish = true
    }
    client.action()
  }

  fun vpaAction() {
    if(isFinish) {
      println("Vpa【开始】跳出")
      Thread.sleep(500)
      System.err.println("Vpa【完成】跳出")
    }
  }

  @Test
  fun testCoroutines() {
    viceVpaAcionByAidl()
    while (true) {
      vpaAction()
      if(isFinish)
        break
      Thread.sleep(100)
    }
  }
}