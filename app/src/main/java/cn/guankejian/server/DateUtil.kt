package cn.guankejian.server

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

open class DateUtil {

  companion object {
    var FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss"

    /**
     * 掉此方法输入所要转换的时间输入例如（"2017-11-01 22:11:00"）返回时间戳
     *
     * @param time
     * @return 时间戳
     */
    @Throws(ParseException::class)
    fun dateToStamp(time: String): Long {
      val sdr = SimpleDateFormat(FORMAT_YMDHMS, Locale.CHINA)
      val date: Date = sdr.parse(time)
      return date.getTime()
    }

    /**
     * 将时间戳转换为时间
     */
    fun stampToDate(lt: Long): String {
      val res: String
      val simpleDateFormat = SimpleDateFormat(FORMAT_YMDHMS, Locale.CHINA)
      val date = Date(lt)
      res = simpleDateFormat.format(date)
      return res
    }

    fun isToday(lt: Long): Boolean {
      val currentCalendar = Calendar.getInstance()
      val currentYear = currentCalendar[Calendar.YEAR]
      val currentMonth = currentCalendar[Calendar.MONTH]
      val currentDay = currentCalendar[Calendar.DAY_OF_YEAR]

      val targetCalendar = Calendar.getInstance()
      targetCalendar.timeInMillis = lt
      val targetYear = targetCalendar[Calendar.YEAR]
      val targetMonth = targetCalendar[Calendar.MONTH]
      val targetDay = targetCalendar[Calendar.DAY_OF_YEAR]

      return currentYear == targetYear && currentMonth == targetMonth && currentDay == targetDay

    }
  }
}