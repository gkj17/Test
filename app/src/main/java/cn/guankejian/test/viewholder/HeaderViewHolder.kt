package cn.guankejian.test.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.guankejian.test.bean.MZTag
import cn.guankejian.test.databinding.HeaderVhBinding
import cn.guankejian.test.databinding.MeinvViewholderTagBinding

class HeaderViewHolder(val binding: HeaderVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): HeaderViewHolder {
            val binding: HeaderVhBinding =
                HeaderVhBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return HeaderViewHolder(binding)
        }


    }

    fun bind(data: String) {
        binding.text.text = data
    }
}
