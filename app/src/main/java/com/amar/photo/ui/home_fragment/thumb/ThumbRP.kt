package com.amar.photo.ui.home_fragment.thumb

interface ThumbRP {

    suspend fun getThumbEffects(position: Int): ArrayList<Thumb>
    suspend fun loadLomoEffects(): ArrayList<Thumb>
    suspend fun loadPolaroicEffects(): ArrayList<Thumb>
    suspend fun loadArtisticEffects(): ArrayList<Thumb>

}