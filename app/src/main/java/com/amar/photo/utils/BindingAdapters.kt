package com.amar.photo.utils

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView


@BindingAdapter("imageUrl")
fun loadImageFromAsset(view: RoundedImageView, url: String?) {
    Glide.with(view.context)
        .load(url)
        .into(view)
}

@BindingAdapter("imageFromAsset")
fun RoundedImageView.imageFromAsset(url: String){
    Glide.with(this.context)
        .load(url)
        .into(this)
}