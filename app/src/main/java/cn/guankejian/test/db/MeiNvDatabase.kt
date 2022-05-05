package cn.guankejian.test.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.guankejian.test.bean.ConstantKey
import cn.guankejian.test.bean.MZTag

@Database(
    entities = [
        MZTag::class,
    ConstantKey::class
    ], version = 1, exportSchema = false
)
abstract class MeiNvDatabase : RoomDatabase() {
    abstract fun mzTagDao(): MZTagDao
    abstract fun constantDao(): ConstantDao

    companion object {
        private const val DATABASE_NAME = "db"
        const val TABLE_NAME_MZ_TAG = "mz_tag"

        private var sDatabase: MeiNvDatabase? = null

        @Synchronized
        fun getInstance(application: Application?): MeiNvDatabase {
            if (sDatabase == null) {
                sDatabase =
                    Room.databaseBuilder(application!!, MeiNvDatabase::class.java, DATABASE_NAME)
                        .build()
            }
            return sDatabase!!
        }


    }
}