package com.amar.photo.ui.fragment.filter_fragment

import android.graphics.Bitmap
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter


data class Filter(
    val name: String,
    val filter: GPUImageFilter,
    val filterPrev: Bitmap,
)
