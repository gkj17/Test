package cn.guankejian.server

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MockKTest {
  companion object{
    const val EXPECT_NAME = "官科健"
    const val EXPECT_AGE = 26
    const val EXPECT_SEX = true
    const val EXPECT_PHONE = 13120391234L
  }

  val sp = mockk<SharedPreferences>()

  @Before
  fun before(){
    every{sp.getString("name", "")} returns EXPECT_NAME
    every{sp.getInt("age", -1)} returns EXPECT_AGE
    every{sp.getBoolean("sex", true)} returns EXPECT_SEX
    every{sp.getLong("phone", -1)} returns EXPECT_PHONE
  }
  @Test
  fun test() {

    val actualName = sp.getString("name", "")
    val actualAge = sp.getInt("age", -1)
    val actualSex = sp.getBoolean("sex", true)
    val actualPhone = sp.getLong("phone", -1)

    Assert.assertEquals("名字读取失败", actualName, SharePreferenceTestUsingKotlin.EXPECT_NAME)
    Assert.assertEquals("年龄读取失败", actualAge, SharePreferenceTestUsingKotlin.EXPECT_AGE)
    Assert.assertEquals("性别读取失败", actualSex, SharePreferenceTestUsingKotlin.EXPECT_SEX)
    Assert.assertEquals("手机读取失败", actualPhone, SharePreferenceTestUsingKotlin.EXPECT_PHONE)
  }
}