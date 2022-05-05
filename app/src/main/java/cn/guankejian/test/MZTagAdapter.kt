package cn.guankejian.test

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import cn.guankejian.test.bean.MZTag
import cn.guankejian.test.base.BasePagingDataAdapter
import cn.guankejian.test.bean.TagUIModel
import cn.guankejian.test.viewholder.BaseViewHolder
import cn.guankejian.test.viewholder.EndViewHolder
import cn.guankejian.test.viewholder.SepViewHolder
import cn.guankejian.test.viewholder.TagViewHolder
import javax.inject.Inject

class MZTagAdapter @Inject constructor() : BasePagingDataAdapter<TagUIModel, BaseViewHolder>(
    COMPARTOR
) {
    companion object {

        private val COMPARTOR = object : DiffUtil.ItemCallback<TagUIModel>() {
            override fun areItemsTheSame(oldItem: TagUIModel, newItem: TagUIModel): Boolean {
                return if (oldItem is TagUIModel.Item && newItem is TagUIModel.Item) {
                    oldItem.item.id == newItem.item.id
                } else if (oldItem is TagUIModel.Sep && newItem is TagUIModel.Sep)
                    oldItem.item == newItem.item
                else if (oldItem is TagUIModel.End && newItem is TagUIModel.End)
                    true
                else
                    oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TagUIModel, newItem: TagUIModel): Boolean {
                return if (oldItem is TagUIModel.Item && newItem is TagUIModel.Item) {
                    return oldItem.item.toString() == newItem.item.toString()
                } else if (oldItem is TagUIModel.Sep && newItem is TagUIModel.Sep)
                    oldItem.item == newItem.item
                else if (oldItem is TagUIModel.End && newItem is TagUIModel.End)
                    true
                else
                    oldItem == newItem
            }
        }

        private const val TYPE_ITEM = 0
        private const val TYPE_SEP = 1
        private const val TYPE_END = 2
    }

    override fun getItemViewType(position: Int): Int {
        val ret = getItem(position) ?: return TYPE_ITEM
        return when (ret) {
            is TagUIModel.Item -> TYPE_ITEM
            is TagUIModel.Sep -> TYPE_SEP
            is TagUIModel.End -> TYPE_END
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val bean = getItem(position)?:return

        when (holder) {
            is TagViewHolder -> bean.let { holder.bind((it as TagUIModel.Item).item) }
            is SepViewHolder -> bean.let { holder.bind((it as TagUIModel.Sep).item) }
//            is EndViewHolder -> bean.let { holder.bind((it as AccountUIModel.YMDItem).item) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_ITEM -> TagViewHolder.create(parent)
            TYPE_SEP -> SepViewHolder.create(parent)
            else/*TYPE_END*/ -> EndViewHolder.create(parent)
        }
    }
}