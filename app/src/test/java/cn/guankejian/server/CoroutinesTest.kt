package cn.guankejian.server

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CoroutinesTest {

  suspend fun fun1():String{
    delay(1000)
    return "1"
  }

  suspend fun fun2():String{
    delay(1500)
    return "2"
  }


  @Test
  fun testCoroutines() = runTest {
    val fun1 = async{ fun1()}
    val fun2 = async{ fun2()}
    println(fun1.await())
    println(fun2.await())
  }
}