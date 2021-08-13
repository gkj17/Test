package cn.guankejian.test.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import cn.guankejian.test.db.MeiNvDatabase

@Entity(tableName = MeiNvDatabase.TABLE_NAME_MZ_TAG)
class MZTag(
    val img:String,
    val title:String,
    val content:String,
    @PrimaryKey(autoGenerate = true) val id:Int
)

class AddMZTag(
    val img:String,
    val title:String,
    val content:String,
)