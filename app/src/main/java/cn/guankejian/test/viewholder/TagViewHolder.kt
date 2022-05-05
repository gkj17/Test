package cn.guankejian.test.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import cn.guankejian.test.bean.MZTag
import cn.guankejian.test.databinding.MeinvViewholderTagBinding

class TagViewHolder(val binding: MeinvViewholderTagBinding) :
    BaseViewHolder(binding.root) {

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

    fun bind(item: MZTag) {
        binding.data = item
        binding.type.text = item.type
        binding.remark.apply{
            isVisible = item.remark.isNotEmpty()
        }
        if(binding.remark.isVisible)
            binding.remark.text = item.remark
        binding.price.text = "￥${item.price}"

        binding.digest.apply{
            isVisible = item.digest.isNotEmpty()
        }
        binding.digest.text = item.digest
    }
}
