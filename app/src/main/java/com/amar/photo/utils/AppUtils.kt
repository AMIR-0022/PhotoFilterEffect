package com.amar.photo.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.amar.photo.ui.activity.gallery_activity.ImageGalleryActivity
import com.amar.photo.ui.fragment.home_fragment.thumb.Thumb
import com.amar.photo.utils.AppConstants.TAG
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

object AppUtils {

    fun navigateToFragment(resId: Int, navController: NavController) {
        navController.navigate(resId, null,
            NavOptions.Builder()
                .setPopUpTo(navController.currentDestination!!.id, true)
                .build())
    }


    fun resizeBitmap(input: Bitmap, destWidth: Int, destHeight: Int, rotation: Int = 0): Bitmap? {
        var bmp: Bitmap? = null
        Log.d(TAG, "ResizeBitmap: Starting", )
        val job = CoroutineScope(Dispatchers.IO).async {
            var dstWidth = destWidth
            var dstHeight = destHeight
            val srcWidth = input.width
            val srcHeight = input.height
            if (rotation == 90 || rotation == 270) {
                dstWidth = destHeight
                dstHeight = destWidth
            }
            var needsResize = false
            val p: Float
            if (srcWidth > dstWidth || srcHeight > dstHeight) {
                needsResize = true
                if (srcWidth > srcHeight && srcWidth > dstWidth) {
                    p = dstWidth.toFloat() / srcWidth.toFloat()
                    dstHeight = (srcHeight * p).toInt()
                } else {
                    p = dstHeight.toFloat() / srcHeight.toFloat()
                    dstWidth = (srcWidth * p).toInt()
                }
            } else {
                dstWidth = srcWidth
                dstHeight = srcHeight
            }

            if (needsResize || rotation != 0) {
                bmp = if (rotation == 0) {
                    Bitmap.createScaledBitmap(input, dstWidth, dstHeight, true)
                } else {
                    val matrix = Matrix()
                    matrix.postScale(dstWidth.toFloat() / srcWidth, dstHeight.toFloat() / srcHeight)
                    matrix.postRotate(rotation.toFloat())
                    Bitmap.createBitmap(input, 0, 0, srcWidth, srcHeight, matrix, true)
                }

            } else {
                bmp = input
            }
            Log.d(TAG, "Final bitmap done")
        }
        runBlocking {

            job.join()
        }
        Log.d(TAG, "ResizeBitmap: Ending", )
        return bmp
    }


    fun preDownloadImg(activity: Activity, progressBar: ProgressBar, thumb: Thumb,) {
        progressBar.visibility = View.VISIBLE
        Log.d(TAG, "onPreLoad: starting to preload image")
        Glide.with(activity.baseContext)
            .load(Uri.parse(thumb.mask))
            .into(object : CustomTarget<Drawable?>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                    progressBar.visibility = View.GONE
                    downloadedFrame = resource

                    potterDuffMode = thumb.blend.toInt()
                    if (isTemplateSelect) {
                        activity.finish()
                    } else {
                        thumb.mask.let {
                            thumb.isDownloaded = true
                            downloadedFrameLink = it

                            // jump to next activity
                            val intent = Intent(activity, ImageGalleryActivity::class.java)
                            activity.startActivity(intent)
                        }
                    }

                }
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Log.d(TAG, "onPreLoadFailed: failed to preload image")
                    progressBar.visibility = View.GONE
                }
            })
    }


}


