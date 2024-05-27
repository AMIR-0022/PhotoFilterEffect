package com.amar.photo.ui.fragment.filter_fragment

import android.graphics.Bitmap
import android.net.Uri

interface FilterRP {

    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?

    suspend fun getImageFilters(image: Bitmap): ArrayList<Filter>

}