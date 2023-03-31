package cn.guankejian.test.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.guankejian.test.bean.MZTag
import cn.guankejian.test.databinding.FooterVhBinding
import cn.guankejian.test.databinding.HeaderVhBinding
import cn.guankejian.test.databinding.MeinvViewholderTagBinding

class FooterViewHolder(val binding: FooterVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): FooterViewHolder {
            val binding: FooterVhBinding =
                FooterVhBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return FooterViewHolder(binding)
        }


    }

    fun bind(data: String) {
        binding.text.text = data
    }
}
