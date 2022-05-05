package cn.guankejian.test.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import cn.guankejian.test.databinding.ViewholderEndBinding

class EndViewHolder(val binding: ViewholderEndBinding) :
    BaseViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup): EndViewHolder {
            val binding =
                ViewholderEndBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return EndViewHolder(binding)
        }

    }

}