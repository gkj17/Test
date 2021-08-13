package cn.guankejian.test

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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



    fun captureTag() = handleFlowPaging(KEY_MZ_TAG) {
        repository.captureTag()
    }



    @Suppress("UNCHECKED_CAST")
    fun <T : Any> handleFlowPaging(
        key: String,
        listener: () -> Flow<PagingData<T>>
    ): Flow<PagingData<T>> {
        resultMap[key]?.let {
            return it as Flow<PagingData<T>>
        }

        val newResult = listener()
        resultMap[key] = newResult.cachedIn(viewModelScope)
        return newResult
    }

}