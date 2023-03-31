package cn.guankejian.test

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cn.guankejian.test.bean.MZTag
import cn.guankejian.test.base.BasePagingDataAdapter
import cn.guankejian.test.bean.UIModel
import cn.guankejian.test.viewholder.FooterViewHolder
import cn.guankejian.test.viewholder.HeaderViewHolder
import cn.guankejian.test.viewholder.SepViewHolder
import cn.guankejian.test.viewholder.TagViewHolder
import javax.inject.Inject

class MZTagAdapter @Inject constructor() : BasePagingDataAdapter<UIModel, RecyclerView.ViewHolder>(
    COMPARTOR
) {
    companion object {
        private const val TYPE_ICON = 0
        private const val TYPE_FOOTER = 1
        private const val TYPE_HEADER = 2
        private const val TYPE_SEP = 3
        private val COMPARTOR = object : DiffUtil.ItemCallback<UIModel>() {
            override fun areItemsTheSame(oldItem: UIModel, newItem: UIModel): Boolean {
                return if (oldItem is UIModel.IconItem && newItem is UIModel.IconItem) {
                    oldItem.item.title == newItem.item.title
                }
                else if (oldItem is UIModel.HeaderItem && newItem is UIModel.HeaderItem)
                    oldItem.item == newItem.item
                else if (oldItem is UIModel.FooterItem && newItem is UIModel.FooterItem)
                    oldItem.item == newItem.item
                else if (oldItem is UIModel.SepItem && newItem is UIModel.SepItem)
                    oldItem.item == newItem.item
                else
                    oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UIModel, newItem: UIModel): Boolean {
                return if (oldItem is UIModel.IconItem && newItem is UIModel.IconItem) {
                    oldItem.item.title == newItem.item.title
                }
                else if (oldItem is UIModel.HeaderItem && newItem is UIModel.HeaderItem)
                    oldItem.item == newItem.item
                else if (oldItem is UIModel.FooterItem && newItem is UIModel.FooterItem)
                    oldItem.item == newItem.item
                else if (oldItem is UIModel.SepItem && newItem is UIModel.SepItem)
                    oldItem.item == newItem.item
                else
                    oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

//        Log.e("TAG","bind ${position}")
        val bean = getItem(position)?:return

        when (holder) {
            is TagViewHolder -> {
                bean.let { holder.bind((it as UIModel.IconItem).item) }
            }
            is FooterViewHolder -> bean.let { holder.bind((it as UIModel.FooterItem).item) }
            is HeaderViewHolder -> bean.let { holder.bind((it as UIModel.HeaderItem).item) }
            is SepViewHolder -> bean.let { holder.bind((it as UIModel.SepItem).item) }
        }

    }

    override fun getItemViewType(position: Int): Int {
        val ret = getItem(position) ?: return TYPE_ICON
        return when (ret) {
            is UIModel.IconItem -> TYPE_ICON
            is UIModel.FooterItem -> TYPE_FOOTER
            is UIModel.HeaderItem -> TYPE_HEADER
            is UIModel.SepItem -> TYPE_SEP
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_ICON->TagViewHolder.create(parent)
            TYPE_FOOTER->FooterViewHolder.create(parent)
            TYPE_HEADER->HeaderViewHolder.create(parent)
            TYPE_SEP-> SepViewHolder.create(parent)
            else-> TagViewHolder.create(parent)
        }
    }
}