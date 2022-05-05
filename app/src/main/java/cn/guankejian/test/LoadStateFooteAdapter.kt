package cn.guankejian.test

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import cn.guankejian.test.viewholder.LoadStateFooterViewHolder

class LoadStateFooteAdapter(
        private val retry:()->Unit
): LoadStateAdapter<LoadStateFooterViewHolder>(){
    override fun onBindViewHolder(holder: LoadStateFooterViewHolder, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            loadState: LoadState
    ): LoadStateFooterViewHolder {
        return LoadStateFooterViewHolder(parent, retry)
    }
}