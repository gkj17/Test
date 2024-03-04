package cn.guankejian.server.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val img: String,
    val source: String,
    val link: String,
    val detail: String,
    val isFavorited: Boolean = false // 新增收藏状态，默认为false
)
