package cn.guankejian.test.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cn.guankejian.test.RoomConverter

@Entity(tableName = "constant_key")
@TypeConverters(RoomConverter::class)
data class ConstantKey(
    @PrimaryKey
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val keys: String,
    val value: Any = "",
    val last_upload: Long = System.currentTimeMillis()
)