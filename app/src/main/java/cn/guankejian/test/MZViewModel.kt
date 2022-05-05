package cn.guankejian.test

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import cn.guankejian.test.bean.MZTag
import cn.guankejian.test.bean.TagUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MZViewModel @Inject constructor(
    application: Application,
    val repository: Repository
) : AndroidViewModel(application) {


    val resultMap = hashMapOf<String, Flow<PagingData<*>>>()


    companion object {
        const val KEY_MZ_TAG = "KEY_MZ_TAG"
    }



    fun captureTag():Flow<PagingData<TagUIModel>>{
        return repository.captureTag().map { pagingData->
            pagingData.map {
                TagUIModel.Item(it)
            }.insertSeparators { before: TagUIModel?, after: TagUIModel? ->
                if (after != null && before != null) {
                    val tmpAfter = (after as TagUIModel.Item).item
                    val tmpBefore = (before as TagUIModel.Item).item
                    if (tmpAfter.year * 10000 + tmpAfter.month * 100 + tmpAfter.day != tmpBefore.year * 10000 + tmpBefore.month * 100 + tmpBefore.day) {
                        val ret = "${tmpAfter.year}/${tmpAfter.month}/${tmpAfter.day}"
                        TagUIModel.Sep(ret)
                    } else {
                        null
                    }
                } else if (before == null && after == null) {
                    null
                } else if (after == null && before != null) {
                    TagUIModel.End("")
                } else if (before == null && after != null) {
                    val tmp = (after as TagUIModel.Item).item
                    val ret = "${tmp.year}/${tmp.month}/${tmp.day}"
                    TagUIModel.Sep(ret)
                } else {
                    null
                }
            }
        }
    }


/*
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> handleFlowPaging(
        key: String,
        listener: () -> Flow<PagingData<T>>
    ): Flow<PagingData<T>> {
        resultMap[key]?.let {
            return it as Flow<PagingData<T>>
        }

        val newResult = listener()
        resultMap[key] = newResult
        return newResult
    }*/

}