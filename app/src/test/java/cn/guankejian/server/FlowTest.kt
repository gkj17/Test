package cn.guankejian.server

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

//通过加锁实现
class FlowTest {

  private val _viceVpaJumpOutDoneFlow =
    MutableSharedFlow<Boolean>()

  //地球上的Vpa
  fun viceVpaAcionByAidl(scope: CoroutineScope) {
    val client = ClientServer()
    client.setCallBack {
      scope.launch{
        _viceVpaJumpOutDoneFlow.emit(true)
      }
    }
    client.action()
  }

  suspend fun vpaAction() {
    if (_viceVpaJumpOutDoneFlow.first()) {
      println("Vpa【开始】跳出")
      delay(1500)
      System.err.println("Vpa【完成】跳出")
    }
  }

  @Test
  fun main() = runTest {
    viceVpaAcionByAidl(this)
    vpaAction()
  }
}

