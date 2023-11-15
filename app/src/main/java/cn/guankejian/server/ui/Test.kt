package cn.guankejian.server.ui

import cn.guankejian.server.second_part.bean.filterTallerThan
import cn.guankejian.server.util.daysUntil
import cn.guankejian.server.util.isBefore
import cn.guankejian.server.util.plusDay
import org.junit.Test
import java.time.LocalDate

internal class Test {

  @Test
  fun testSecond() {

    val a by lazy { LocalDate.of(2023, 11, 5) }
    val b = LocalDate.of(2023, 11, 20)
    val c = a daysUntil b
    val d = a isBefore b
    println(c)
    println(d)


  }
}