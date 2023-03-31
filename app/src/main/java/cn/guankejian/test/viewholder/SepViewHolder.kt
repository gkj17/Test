package cn.guankejian.test.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.guankejian.test.bean.MZTag
import cn.guankejian.test.databinding.HeaderVhBinding
import cn.guankejian.test.databinding.MeinvViewholderTagBinding
import cn.guankejian.test.databinding.SepVhBinding

class SepViewHolder(val binding: SepVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): SepViewHolder {
            val binding: SepVhBinding =
                SepVhBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return SepViewHolder(binding)
        }


    }

    fun bind(data: String) {
        binding.text.text = data
    }
}
