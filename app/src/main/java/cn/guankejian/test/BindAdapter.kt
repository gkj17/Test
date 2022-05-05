package cn.guankejian.test

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

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


    @JvmStatic
    @BindingAdapter(value = ["selected"], requireAll = false)
    fun selected(view: View, selected: Boolean) {
        view.isSelected = selected
    }



    @JvmStatic
    @BindingAdapter("categoryIcon")
    fun categoryIcon(view: ShapeableImageView, name: String?) {
        view.setImageResource(CategoryUtil.resId(name ?: "其他"))
    }
}