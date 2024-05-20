package com.amar.photo.ui.activity.gallery_activity

import com.amar.photo.ui.fragment.folder_fragment.GalleryFolder
import com.amar.photo.ui.fragment.image_fragment.GalleryImage

interface GalleryRP {

    suspend fun loadGalleryFolders() : ArrayList<GalleryFolder>
    suspend fun loadGalleryImages(path: String) : ArrayList<GalleryImage>

}