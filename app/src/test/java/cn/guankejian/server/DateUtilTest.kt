package cn.guankejian.server

import cn.guankejian.server.DateUtil
import org.junit.After
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

internal class DateUtilTest {

  companion object{
    const val timeStamp = 1508054492999L
    const val time = "2023-11-07 18:00:12"
    @BeforeClass
    @JvmStatic
    fun beforeClass(){
      System.err.println("开始测试")
    }

    @AfterClass
    @JvmStatic
    fun afterClass(){
      System.err.println("结束测试")
    }
  }


  @Before
  fun before(){
    System.err.println("before")
  }

  @After
  fun after(){
    System.err.println("after")
  }

  @Test
  fun dateUtil_IsToday_ReturnTrue() {
    System.err.println("dateUtil_IsToday_ReturnTrue")
    assertTrue("DateUtil.isToday Return True 验证正确",DateUtil.isToday(System.currentTimeMillis()))
  }

  @Test
  fun dateUtil_IsToday_ReturnFalse() {
    System.err.println("dateUtil_IsToday_ReturnFalse")
    assertFalse("DateUtil.isToday Return False 验证正确",DateUtil.isToday(1000000))
  }

  @Test
  fun dateUtil_DateToStamp(){
    assertEquals(1699351212000,DateUtil.dateToStamp(time))
  }

  @Test
  fun dateUtil_StampToDate(){
    assertEquals("2017-10-15 16:01:32",DateUtil.stampToDate(timeStamp))
  }
}