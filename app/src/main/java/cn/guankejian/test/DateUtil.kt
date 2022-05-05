package cn.guankejian.test

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object DateUtil{

    const val ONE_DAY_TIME = 86400000L

    @JvmStatic
    fun million2String(million: Long, format: String = "yyyy/MM/dd HH:mm:ss"): String {
        return SimpleDateFormat(format).format(Date(million))
    }

    @JvmStatic
    fun timeStamp2String(timeStamp: Timestamp): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeStamp)
    }


    val now = GregorianCalendar()
    @JvmStatic
    fun currentYear():Int{
        return now.get(Calendar.YEAR)
    }

    /*
    * 返回月份1-12
    * */
    @JvmStatic
    fun currentMonth():Int{
        return now.get(Calendar.MONTH)+1
    }

    @JvmStatic
    fun currentDay():Int{
        return now.get(Calendar.DAY_OF_MONTH)
    }

    @JvmStatic
    fun getMillion(year:Int,month:Int,day:Int,hour:Int=0,minute:Int=0,second:Int=0): Long {
        val c = GregorianCalendar()
        c.set(Calendar.YEAR,year)
        c.set(Calendar.MONTH,month-1)
        c.set(Calendar.DAY_OF_MONTH,day)
        c.set(Calendar.HOUR,hour)
        c.set(Calendar.MINUTE,minute)
        c.set(Calendar.SECOND,second)
        return c.timeInMillis
    }




}