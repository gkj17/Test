package cn.guankejian.test.bean

import androidx.room.*
import com.google.gson.Gson

@Entity(tableName = "constant_key")
@TypeConverters(RoomConverter::class)
data class ConstantKey(
    @PrimaryKey
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val key: String,
    val value: Any = "",
    val last_upload: Long = System.currentTimeMillis()
)

open class RoomConverter {

    var gson = Gson()

    @TypeConverter
    fun any2String(any: Any): String {
        return gson.toJson(any)
    }

    @TypeConverter
    fun string2Any(value: String): Any {
        return gson.fromJson(value, Any::class.java)
    }
}
