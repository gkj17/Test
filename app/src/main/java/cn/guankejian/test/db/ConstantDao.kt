package cn.guankejian.test.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cn.guankejian.test.bean.ConstantKey

@Dao
interface ConstantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bean: ConstantKey)


    @Query("SELECT * FROM constant_key WHERE `key` = :key")
    suspend fun get(key: String): ConstantKey

}