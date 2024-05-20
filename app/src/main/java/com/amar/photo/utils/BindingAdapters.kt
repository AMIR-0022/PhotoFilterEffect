package com.amar.photo.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.amar.photo.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView


@BindingAdapter("imageFromAsset")
fun RoundedImageView.imageFromAsset(url: String){
    Glide.with(this.context)
        .load(url)
        .into(this)
}

@BindingAdapter("imageFromUrl")
fun AppCompatImageView.imageFromUrl(url: String) {
    Glide.with(this.context)
        .load(url)
        .apply(RequestOptions()
        .fitCenter())
        .centerCrop()
        .into(this)
}

@BindingAdapter("fontFamilyBasedOnChecked")
fun AppCompatTextView.setFontFamilyBasedOnChecked(checked: Boolean) {
    val font = if (checked) {
        androidx.core.content.res.ResourcesCompat.getFont(context, R.font.kodchasan_bold)
    } else {
        androidx.core.content.res.ResourcesCompat.getFont(context, R.font.kodchasan_regular)
    }
    this.typeface = font
}