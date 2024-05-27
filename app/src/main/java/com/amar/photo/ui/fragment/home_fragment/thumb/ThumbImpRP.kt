package com.amar.photo.ui.fragment.home_fragment.thumb

import javax.inject.Inject

class ThumbImpRP @Inject constructor(): ThumbRP {

    override suspend fun getThumbEffects(position: Int): ArrayList<Thumb> {
        val list = ArrayList<Thumb>()
        if (position == 0) {
            list.addAll(loadMaskEffects())
        } else if (position == 1) {
            list.addAll(loadOverlayEffects())
        } else if (position == 2) {
            list.addAll(loadPixelEffects())
        } else if (position == 3){
            list.addAll(loadOverlayEffects())
        }
        return list
    }

    override suspend fun loadMaskEffects(): ArrayList<Thumb> {
        val list = arrayListOf<Thumb>()
        list.add(
            Thumb(
                mask = "file:///android_asset/mask_effects/mask_1.webp",
                cover = "file:///android_asset/mask_thumbs/1.webp",
                blend = "9")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/mask_effects/mask_2.webp",
                cover = "file:///android_asset/mask_thumbs/2.webp",
                blend = "10")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/mask_effects/mask_3.webp",
                cover = "file:///android_asset/mask_thumbs/3.webp",
                blend = "10")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/mask_effects/mask_4.webp",
                cover = "file:///android_asset/mask_thumbs/4.webp",
                blend = "9")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/mask_effects/mask_5.webp",
                cover = "file:///android_asset/mask_thumbs/5.webp",
                blend = "9")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/mask_effects/mask_6.webp",
                cover = "file:///android_asset/mask_thumbs/6.webp",
                blend = "9")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/mask_effects/mask_7.webp",
                cover = "file:///android_asset/mask_thumbs/7.webp",
                blend = "9")
        )
        return list
    }

    override suspend fun loadOverlayEffects(): ArrayList<Thumb> {
        val list = arrayListOf<Thumb>()
        list.add(
            Thumb(
                mask = "file:///android_asset/overlay_effect/overlay_1.webp",
                cover = "file:///android_asset/overlay_thumbs/1.webp",
                blend = "10")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/overlay_effect/overlay_2.webp",
                cover = "file:///android_asset/overlay_thumbs/2.webp",
                blend = "11")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/overlay_effect/overlay_3.webp",
                cover = "file:///android_asset/overlay_thumbs/3.webp",
                blend = "10")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/overlay_effect/overlay_4.webp",
                cover = "file:///android_asset/overlay_thumbs/4.webp",
                blend = "10")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/overlay_effect/overlay_5.webp",
                cover = "file:///android_asset/overlay_thumbs/5.webp",
                blend = "10")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/overlay_effect/overlay_6.webp",
                cover = "file:///android_asset/overlay_thumbs/6.webp",
                blend = "10")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/overlay_effect/overlay_7.webp",
                cover = "file:///android_asset/overlay_thumbs/7.webp",
                blend = "10")
        )
        return list
    }

    override suspend fun loadPixelEffects(): ArrayList<Thumb> {
        val list = arrayListOf<Thumb>()
        list.add(
            Thumb(
                mask = "file:///android_asset/pixel_effect/pixel_1.webp",
                cover = "file:///android_asset/pixel_thumbs/1.webp",
                blend = "9")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/pixel_effect/pixel_2.webp",
                cover = "file:///android_asset/pixel_thumbs/2.webp",
                blend = "11")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/pixel_effect/pixel_3.webp",
                cover = "file:///android_asset/pixel_thumbs/3.webp",
                blend = "9")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/pixel_effect/pixel_4.webp",
                cover = "file:///android_asset/pixel_thumbs/4.webp",
                blend = "9")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/pixel_effect/pixel_5.webp",
                cover = "file:///android_asset/pixel_thumbs/5.webp",
                blend = "9")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/pixel_effect/pixel_6.webp",
                cover = "file:///android_asset/pixel_thumbs/6.webp",
                blend = "9")
        )
        list.add(
            Thumb(
                mask = "file:///android_asset/pixel_effect/pixel_7.webp",
                cover = "file:///android_asset/pixel_thumbs/7.webp",
                blend = "9")
        )
        return list
    }

}