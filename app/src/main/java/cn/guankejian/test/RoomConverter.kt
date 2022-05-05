package cn.guankejian.test

import androidx.core.text.isDigitsOnly
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.sql.Timestamp

val DOUBLE_REGEX = Regex(
    "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
            "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
            "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
            "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*"
)

open class RoomConverter {

    var gson = Gson()

    @TypeConverter
    fun any2String(any: Any): String {
        return gson.toJson(any)
    }

    @TypeConverter
    fun string2Any(value: String): Any {

        if (value.isDigitsOnly()) {
            if (value.toInt() > Int.MAX_VALUE) {
                return value.toLong()
            } else
                return value.toInt()
        } else {
            if (value.contains(".0"))
                DOUBLE_REGEX.find(
                    value
                )?.groupValues?.get(0)?.let {
                    return it.toDouble()
                }
            return gson.fromJson(value, Any::class.java)
        }

    }

    @TypeConverter
    fun list2String(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun string2List(value: String): List<String> {
        return gson.fromJson(value, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun list2Int(list: List<Int>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun int2List(value: String): List<Int> {
        return gson.fromJson(value, object : TypeToken<List<Int>>() {}.type)
    }

    @TypeConverter
    fun timeStamp2Long(timeStamp: Timestamp): Long {
        return timeStamp.time
//        return "${timeStamp.year}-${month}-${timeStamp.day} ${timeStamp.hours}:${timeStamp.minutes}:${timeStamp.seconds}"
    }

    @TypeConverter
    fun long2timeStamp(value: Long): Timestamp {
        /*val year = value.substring(0, 4).toInt()
        val month = value.substring(5, 7).toInt() + 1
        val day = value.substring(8, 10).toInt()
        val hours = value.substring(11, 13).toInt()
        val minutes = value.substring(14, 16).toInt()
        val seconds = value.substring(17, 19).toInt()
        return Timestamp(
            year,
            month,
            day,
            hours,
            minutes,
            seconds, 0
        )*/
        return Timestamp(value)
    }


}