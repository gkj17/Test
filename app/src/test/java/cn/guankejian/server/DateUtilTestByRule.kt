package cn.guankejian.server

import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class DateUtilTestByRule {
  @Mock
  lateinit var util:DateUtil

  @Rule
  @JvmField
  var mockitoRule:MockitoRule = MockitoJUnit.rule()

  //可以用@Rule @JvmField 配合  也可以用@get:Rule

  @Test
  fun generateDateUtilByRule(){
    Assert.assertNotNull(util)
  }
}