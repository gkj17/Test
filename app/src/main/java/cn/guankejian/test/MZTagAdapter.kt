package cn.guankejian.test

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import cn.guankejian.test.bean.MZTag
import cn.guankejian.test.base.BasePagingDataAdapter
import cn.guankejian.test.viewholder.TagViewHolder
import javax.inject.Inject

class MZTagAdapter @Inject constructor() : BasePagingDataAdapter<MZTag, TagViewHolder>(
    COMPARTOR
) {
    companion object {

        private val COMPARTOR = object : DiffUtil.ItemCallback<MZTag>() {
            override fun areItemsTheSame(oldItem: MZTag, newItem: MZTag): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MZTag, newItem: MZTag): Boolean {
                return oldItem.title + oldItem.img == newItem.title + newItem.img
            }
        }
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        getItem(position)?.let {
            holder.bind(it)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder.create(parent)
    }
}