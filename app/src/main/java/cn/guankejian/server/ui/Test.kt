package cn.guankejian.server.ui

import cn.guankejian.server.second_part.bean.Person
import cn.guankejian.server.second_part.bean.filterTallerThan
import cn.guankejian.server.util.daysUntil
import cn.guankejian.server.util.isBefore
import cn.guankejian.server.util.plusDay
import org.junit.Test
import java.time.LocalDate
import kotlin.concurrent.thread

internal class Test {

  @Test
  fun testInfix() {

    val a by lazy { LocalDate.of(2023, 11, 5) }
    val b = LocalDate.of(2023, 11, 20)
    val c = a daysUntil b
    val d = a isBefore b
    println(c)
    println(d)


  }


  @Test
  fun testLazy() {

    val a by lazy(LazyThreadSafetyMode.NONE) {
      repeat((1..10000).count()){
        val b = 100
        val c = 1000
        val d =b*c
      }
      System.currentTimeMillis()
    }

    repeat((1..10).count()) {
      thread(true) {
        println(a)
      }
    }
    Thread.sleep(10000)
  }

  @Test
  fun testSmartCast(){
     fun getPerson() = Person(111.0)
    class Kot{
      var p:Person?=getPerson()
      fun dealP(){
        p?.let{
          println(it.height)
        }

//        if(p != null)
//          println(p.height)  //不行的
      }
    }
  }


  @Test
  fun testOutIn(){
    fun <T> copyIn(dest:Array<in T>, src:Array<T>){
      if(dest.size < src.size){
        throw IndexOutOfBoundsException()
      }else{
        src.forEachIndexed{index,value -> dest[index] = src[index]}
      }
    }

    fun <T> copyOut(dest:Array<T>, src:Array<out T>){
      if(dest.size < src.size){
        throw IndexOutOfBoundsException()
      }else{
        src.forEachIndexed{index,value -> dest[index] = src[index]}
      }
    }


    val dest = arrayOfNulls<Number>(3)
    val src = arrayOf<Double>(1.0,2.0,3.0)
    copyIn(dest, src)
    copyOut(dest, src)

  }


}