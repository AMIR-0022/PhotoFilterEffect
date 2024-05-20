package com.amar.photo.ui.fragment.image_fragment

import java.io.Serializable

data class GalleryImage(
    var imageName: String? = "",
    var imagePath: String? = "",
    var imageSize: String? = "",
    var imageUri: String? = "",
    var selected: Boolean = false,
    var count: Int = 0
) : Serializable
