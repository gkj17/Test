package cn.guankejian.server.ui

import cn.guankejian.server.second_part.bean.Person
import org.junit.Test

internal class Test {

  @Test
  fun testSecond() {
//    for(i in  1..10){
//      println(i)
//    }

    val person = Person()
    person say "hello"

    for(i in  'a'..'d'){
      println(i)
    }


  }
}