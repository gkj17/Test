package cn.guankejian.test.db

import android.annotation.SuppressLint
import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.guankejian.test.bean.ConstantKey

@Database(
    entities = [
        ConstantKey::class], version = 1, exportSchema = false
)
abstract class BaseDatabase : RoomDatabase() {

    abstract fun constantDao(): ConstantDao

    companion object {
        val instance: BaseDatabase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(getApplication(), BaseDatabase::class.java, DATABASE_NAME)
                .build()
        }

        private const val DATABASE_NAME = "base_db"


        @SuppressLint("PrivateApi")
        fun getApplication(): Application {
            return Class.forName("android.app.ActivityThread")
                .getMethod("currentApplication").invoke(null) as Application
        }

    }


}