package cn.guankejian.server

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DateUtilTestByAnnotation {
  @Mock
  lateinit var util:DateUtil

  @Before
  fun before(){
    MockitoAnnotations.initMocks(this)
  }
  @Test
  fun generateDateUtilByMockitoAnnotation(){
    Assert.assertNotNull(util)
  }
}