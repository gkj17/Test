package cn.guankejian.server

import android.content.SharedPreferences
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.anyBoolean
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify


@RunWith(MockitoJUnitRunner::class)
class SharePreferenceTestUsingKotlin {

  companion object{
    const val EXPECT_NAME = "官科健"
    const val EXPECT_AGE = 26
    const val EXPECT_SEX = true
    const val EXPECT_PHONE = 13120391234L
  }

  @Mock
  private lateinit var sp: SharedPreferences

  @Before
  fun initMock() {
    given(sp.getString("name", "")).willReturn(EXPECT_NAME)
    given(sp.getInt("age", -1)).willReturn(EXPECT_AGE)
    given(sp.getBoolean("sex", true)).willReturn(EXPECT_SEX)
    given(sp.getLong("phone", -1)).willReturn(EXPECT_PHONE)
  }

  @Test
  fun generateData() {
    val actualName = sp.getString("name", "")
    val actualAge = sp.getInt("age", -1)
    val actualSex = sp.getBoolean("sex", true)
    val actualPhone = sp.getLong("phone", -1)
    verify(sp, times(1)).getString(anyString(), anyString())
    verify(sp, times(1)).getInt(anyString(), anyInt())
    verify(sp, times(1)).getBoolean(anyString(), anyBoolean())
    verify(sp, times(1)).getLong(anyString(), anyLong())

    assertEquals("名字读取失败", actualName, EXPECT_NAME)
    assertEquals("年龄读取失败", actualAge, EXPECT_AGE)
    assertEquals("性别读取失败", actualSex, EXPECT_SEX)
    assertEquals("手机读取失败", actualPhone, EXPECT_PHONE)

  }
}