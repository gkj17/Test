package cn.guankejian.server.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Query("UPDATE movies SET isFavorited = :isFavorited WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorited: Boolean)

    @Query("SELECT * FROM movies WHERE isFavorited = 1")
    fun getFavoritedMovies(): LiveData<List<Movie>>
}
