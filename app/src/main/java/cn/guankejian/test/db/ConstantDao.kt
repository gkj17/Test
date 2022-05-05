package cn.guankejian.test.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cn.guankejian.test.bean.ConstantKey

@Dao
interface ConstantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bean: ConstantKey)

    @Query("DELETE FROM constant_key")
    suspend fun clear()

    @Query("DELETE FROM constant_key WHERE keys = :key")
    suspend fun delete(key: String)

    @Query("SELECT * FROM constant_key WHERE keys = :key")
    suspend fun get(key:String):ConstantKey

    @Query("SELECT * FROM constant_key WHERE keys = :key")
    suspend fun getNullable(key:String):ConstantKey?

    @Query("SELECT * FROM constant_key WHERE keys = :key")
     fun getLive(key:String): LiveData<ConstantKey>

    @Query("SELECT last_upload FROM constant_key WHERE keys = :key")
    suspend fun lastUpload(key:String):Long?
}