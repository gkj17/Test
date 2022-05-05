package cn.guankejian.test.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import cn.guankejian.test.databinding.ViewholderSepBinding

class SepViewHolder(val binding: ViewholderSepBinding) :
    BaseViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup): SepViewHolder {
            val binding =
                ViewholderSepBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return SepViewHolder(binding)
        }

    }

    fun bind(item: String) {
        binding.content.text = item
    }
}