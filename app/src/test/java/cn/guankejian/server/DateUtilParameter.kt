package cn.guankejian.server

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class DateUtilParameter(
  val time:Long
) {
  companion object{
    @Parameterized.Parameters
    @JvmStatic
    fun getList() = listOf(
      System.currentTimeMillis(),
      10000,
      System.currentTimeMillis()-10000,
      System.currentTimeMillis()-100000,
      20000000
    ).map {
      it.toLong()
    }
  }


  @Test
  fun dateUtil_IsToday_ReturnTrue(){
    Assert.assertTrue(DateUtil.isToday(time))
  }
}