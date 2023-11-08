package cn.guankejian.server

import cn.guankejian.server.DateUtil
import org.junit.After
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

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



  @Test
  fun generateDateUtilByMockito(){
    val util = mock(DateUtil::class.java)
    assertNotNull(util)
  }

}