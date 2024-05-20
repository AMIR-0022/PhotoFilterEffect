package com.amar.photo.ui.activity.gallery_activity

import android.content.Context
import android.provider.MediaStore
import com.amar.photo.ui.fragment.folder_fragment.GalleryFolder
import com.amar.photo.ui.fragment.image_fragment.GalleryImage
import javax.inject.Inject

class GalleryImpRP @Inject constructor(private val context: Context): GalleryRP {

    override suspend fun loadGalleryFolders(): ArrayList<GalleryFolder> {
        val folderList = ArrayList<GalleryFolder>()
        val imagePathList = ArrayList<String>()

        val allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val orderBy = MediaStore.MediaColumns.DATE_MODIFIED + " DESC"

        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID
        )

        // Map to store folder paths to their first images
        val firstImageMap = mutableMapOf<String, String>()

        try {
            val cursor = context.contentResolver.query(allImagesUri, projection, null, null, orderBy)
            if (cursor != null) {
                cursor.moveToFirst()
                do {
                    val folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                    val dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                    var folderPaths = dataPath?.substring(0, dataPath.lastIndexOf("$folder/")).toString()

                    folderPaths = "$folderPaths$folder/"

                    // Store the first image for each folder path
                    if (!firstImageMap.containsKey(folderPaths)) {
                        firstImageMap[folderPaths] = dataPath
                    }

                    if (!imagePathList.contains(folderPaths)) {
                        imagePathList.add(folderPaths)

                        val galleryFolder = GalleryFolder().apply {
                            path = folderPaths
                            folderName = folder
                            firstImage = firstImageMap[folderPaths] ?: ""
                        }

                        if (folder == "Camera")
                            folderList.add(0, galleryFolder)
                        else
                            folderList.add(galleryFolder)
                    }
                } while (cursor.moveToNext())
                cursor.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return folderList
    }

    override suspend fun loadGalleryImages(path: String): ArrayList<GalleryImage> {
        var images = ArrayList<GalleryImage>()

        val allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )
        val orderBy = MediaStore.MediaColumns.DATE_MODIFIED + " ASC"
        val cursor = context.contentResolver.query(allImagesUri, projection,
            MediaStore.Images.Media.DATA + " like ? ", arrayOf("%$path%"), orderBy)

        try {
            cursor?.moveToFirst()
            do {
                val img = GalleryImage()
                img.imageName =
                    cursor?.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                img.imagePath =
                    cursor?.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                img.imageSize =
                    cursor?.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                images.add(img)
            } while (cursor!!.moveToNext())
            cursor.close()
            val reSelection = ArrayList<GalleryImage>()
            for (i in images.size - 1 downTo -1 + 1) {
                reSelection.add(images[i])
            }
            images = reSelection
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return images
    }


}