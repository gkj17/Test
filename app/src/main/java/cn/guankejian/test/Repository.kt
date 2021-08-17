package cn.guankejian.test

import android.app.Application
import cn.guankejian.test.bean.ConstantKey
import cn.guankejian.test.db.BaseDatabase
import cn.guankejian.test.db.ConstantDao
import javax.inject.Inject

class Repository @Inject constructor(application: Application) {

    val constantDao: ConstantDao = BaseDatabase.instance.constantDao()

    suspend fun save(){
        constantDao.insert(ConstantKey("test",1))
    }

    suspend fun get():ConstantKey{
        return constantDao.get("test")
    }

}