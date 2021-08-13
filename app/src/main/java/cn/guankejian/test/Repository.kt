package cn.guankejian.test

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import cn.guankejian.test.bean.MZTag
import cn.guankejian.test.db.MeiNvDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(application: Application) {
    val db: MeiNvDatabase = MeiNvDatabase.getInstance(application)

    @ExperimentalPagingApi
    fun captureTag(
    ): Flow<PagingData<MZTag>> {
        return Pager(
            config = PagingConfig(
                pageSize = 4
            ),
            remoteMediator = TagMediator(db)
        ) {
            db.mzTagDao().getAll()
        }
            .flow
    }


}