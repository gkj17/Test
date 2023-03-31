package cn.guankejian.test

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import cn.guankejian.test.bean.MZTag
import cn.guankejian.test.bean.UIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MZViewModel @Inject constructor(
    application: Application,
    val repository: Repository
) : AndroidViewModel(application) {

    suspend fun captureTag():Flow<PagingData<UIModel>>{
        val head1 = getHeader(1)
        val head2 = getHeader(2)
        val footer1 = getFooter(1)
        val footer2 = getFooter(2)


        val list = repository.captureTag()
            .map{pagingData->
//                var count = 1;
                pagingData.map<MZTag,UIModel> {
                    UIModel.IconItem(it)
                }

                    .insertSeparators { before: UIModel?, after: UIModel? ->
                        when {
                            before == null && after != null ->
                                null
                            before != null && after != null ->
                                UIModel.SepItem(item="Separators for ${(after as UIModel.IconItem).item.title}")
                            after == null ->
                                UIModel.FooterItem("Separators footer")
                            else -> null
                        }
                    }
                    .insertHeaderItem(item = UIModel.HeaderItem(head1))
                    .insertHeaderItem(item = UIModel.HeaderItem(head2))
                    .insertFooterItem(item = UIModel.FooterItem(footer1))
                    .insertFooterItem(item = UIModel.FooterItem(footer2))

            }

        return list

    }

    suspend fun getHeader(index:Int):String {
        delay(100)
        return "This is the Header${index}"
    }
    suspend fun getFooter(index:Int):String {
        delay(100)
        return "This is the Footer${index}"
    }





}