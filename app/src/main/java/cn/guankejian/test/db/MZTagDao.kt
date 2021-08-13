package cn.guankejian.test.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cn.guankejian.test.bean.AddMZTag
import cn.guankejian.test.bean.MZTag

@Dao
interface MZTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = MZTag::class)
    suspend fun insert(list: List<AddMZTag>)

    @Query("DELETE FROM `mz_tag`")
    suspend fun clear()

    @Query("SELECT * FROM `mz_tag` ORDER BY id ASC")
    fun getAll(): PagingSource<Int, MZTag>

}