package cn.guankejian.test.base

import android.view.View
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagingDataAdapter<T:Any, VH:RecyclerView.ViewHolder>(
        compartor : DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, VH>(compartor) {
     val checkPositionIds: ArrayList<Int> = ArrayList()
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemChildClickListener: OnItemChildClickListener? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener { setOnItemClick(it,holder.absoluteAdapterPosition) }
    }

    open fun getItemData(postion:Int): T? {
        return getItem(postion)
    }
    protected open fun setOnItemClick(v: View, position: Int) {
        mOnItemClickListener?.onItemClick(this, v, position)
    }

    protected open fun setOnItemChildClick(v: View, position: Int) {
        mOnItemChildClickListener?.onItemChildClick(this, v, position)
    }


    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.mOnItemClickListener = listener
    }
    fun setOnItemChildClickListener(listener: OnItemChildClickListener?) {
        this.mOnItemChildClickListener = listener
    }

    fun interface OnItemClickListener{
        fun onItemClick(adapter: BasePagingDataAdapter<*,*>, view: View, position: Int)
    }

   fun interface OnItemChildClickListener{
        fun onItemChildClick(adapter: BasePagingDataAdapter<*,*>, view: View, position: Int)
    }

}