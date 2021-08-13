package cn.guankejian.test.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.guankejian.test.bean.MZTag
import cn.guankejian.test.databinding.MeinvViewholderTagBinding

class TagViewHolder(val binding: MeinvViewholderTagBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): TagViewHolder {
            val binding: MeinvViewholderTagBinding =
                MeinvViewholderTagBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return TagViewHolder(binding)
        }


    }

    fun bind(data: MZTag) {
        binding.data = data
    }
}
