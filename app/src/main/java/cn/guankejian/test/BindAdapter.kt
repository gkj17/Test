package cn.guankejian.test

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindAdapter {
    @JvmStatic
    @BindingAdapter(
        value = [
            "imageUrl"],
        requireAll = false
    )
    fun loadImage(
        imageView: ImageView,
        imageUrl: String?,
    ) {
        Glide
            .with(imageView.context)
            .asDrawable()
            .load(imageUrl)
            .into(imageView)
    }
}