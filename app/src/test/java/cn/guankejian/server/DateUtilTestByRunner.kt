package cn.guankejian.server

import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DateUtilTestByRunner {
  @Mock
  private lateinit var util:DateUtil

  @Test
  fun generateDateUtilByRunner(){
    assertNotNull(util)
  }
}