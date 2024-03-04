package cn.guankejian.server.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlaybackHistoryDao {
    @Insert
    fun insertPlaybackHistory(playbackHistory: PlaybackHistory)

    @Query("SELECT * FROM PlaybackHistory WHERE movieId = :movieId ORDER BY watchedDate DESC")
    fun getPlaybackHistoryByMovieId(movieId: Int): LiveData<List<PlaybackHistory>>

    @Query("DELETE FROM PlaybackHistory WHERE movieId = :movieId")
    fun deletePlaybackHistoryForMovie(movieId: Int)
}
