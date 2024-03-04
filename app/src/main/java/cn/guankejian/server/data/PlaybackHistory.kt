package cn.guankejian.server.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaybackHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val movieId: Int,
    val watchedDate: Long, // 记录观看日期的时间戳
    val watchedTime: Long // 观看视频的时间戳，表示用户观看到的具体时间点
)
