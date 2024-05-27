package com.amar.photo.ui.fragment.home_fragment.thumb

interface ThumbRP {

    suspend fun getThumbEffects(position: Int): ArrayList<Thumb>
    suspend fun loadMaskEffects(): ArrayList<Thumb>
    suspend fun loadOverlayEffects(): ArrayList<Thumb>
    suspend fun loadPixelEffects(): ArrayList<Thumb>

}