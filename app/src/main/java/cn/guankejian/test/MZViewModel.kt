package cn.guankejian.test

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import cn.guankejian.test.bean.ConstantKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MZViewModel @Inject constructor(
    application: Application,
    val repository: Repository
) : AndroidViewModel(application) {


    fun save() {
        viewModelScope.launch {
            repository.save()
        }
    }

    suspend fun get(): ConstantKey {
        return repository.get()
    }
}