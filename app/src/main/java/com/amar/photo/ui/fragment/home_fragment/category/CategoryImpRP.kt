package com.amar.photo.ui.fragment.home_fragment.category

import javax.inject.Inject

class CategoryImpRP @Inject constructor(): CategoryRP {

    override suspend fun getCategory(): ArrayList<Category> {
        val list = ArrayList<Category>()
        list.clear()
        list.add(
            Category(
                id = 0,
                title = "Mask Effects",
                image = "file:///android_asset/mask_thumbs/1.webp",
                isChecked = true
            )
        )
        list.add(
            Category(
                id = 1,
                title = "Overlay Effects",
                image = "file:///android_asset/mask_thumbs/2.webp",
            )
        )
        list.add(
            Category(
                id = 2,
                title = "Pixel Effect",
                image = "file:///android_asset/mask_thumbs/3.webp",
            )
        )
        list.add(
            Category(
                id = 3,
                title = "Artistic Effects",
                image = "file:///android_asset/mask_thumbs/4.webp",
            )
        )
        return list
    }

}