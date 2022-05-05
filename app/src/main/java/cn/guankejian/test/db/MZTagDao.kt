package cn.guankejian.test.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cn.guankejian.test.bean.MZTag

@Dao
interface MZTagDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(list: List<MZTag>)

    @Query("DELETE FROM `mz_tag`")
    suspend fun clear()

    @Query("SELECT * FROM `mz_tag` ORDER BY year*10000+month*100+day DESC, id DESC")
    fun getAll(): PagingSource<Int, MZTag>

}