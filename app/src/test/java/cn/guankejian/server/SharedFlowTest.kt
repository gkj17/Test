package cn.guankejian.server

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SharedFlowTest {
  @Test
  fun test1() = runTest {
    runBlocking {
      // 创建一个 SharedFlow
      val sharedFlow = MutableSharedFlow<Int>()

      // 启动一个协程来发送数据
      GlobalScope.launch {
        var counter = 0
        while (true) {
          sharedFlow.emit(counter)
          counter++
          delay(1000)
        }
      }

      // 在多个不同的协程中订阅这个 SharedFlow
      repeat(3) { subscriberId ->
        GlobalScope.launch {
          sharedFlow.collect { value ->
            println("Subscriber $subscriberId received: $value")
          }
        }
      }

      while(true){
        val a = 1
      }
    }
  }
}