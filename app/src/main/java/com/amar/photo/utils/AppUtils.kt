package com.amar.photo.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.amar.photo.ui.activity.gallery_activity.ImageGalleryActivity
import com.amar.photo.ui.fragment.home_fragment.thumb.Thumb
import com.amar.photo.utils.Constants.TAG
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.Calendar

object AppUtils {

    fun navigateToFragment(resId: Int, navController: NavController) {
        navController.navigate(resId, null,
            NavOptions.Builder()
                .setPopUpTo(navController.currentDestination!!.id, true)
                .build())
    }


    fun resizeBitmap(input: Bitmap, destWidth: Int, destHeight: Int, rotation: Int = 0): Bitmap? {
        var bmp: Bitmap? = null
        Log.d(TAG, "ResizeBitmap: Starting")
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
        Log.d(TAG, "ResizeBitmap: Ending")
        return bmp
    }


    fun preDownloadImg(activity: Activity, progressBar: ProgressBar, thumb: Thumb) {
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

    fun preDownloadImg(context: Context, progressBar: ProgressBar, thumb: Thumb, callback: () -> Unit) {
        progressBar.visibility = View.VISIBLE
        Log.d(TAG, "onPreLoad: starting to preload image")
        Glide.with(context)
            .load(Uri.parse(thumb.mask))
            .into(object : CustomTarget<Drawable?>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                    progressBar.visibility = View.GONE

                    downloadedFrame = resource
                    potterDuffMode = thumb.blend.toInt()

                    if (isTemplateSelect) {

                    } else {
                        thumb.mask.let {
                            thumb.isDownloaded = true
                            downloadedFrameLink = it
                            callback.invoke()
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



    fun shareImage(imageUri: Uri, context: Context) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, imageUri)
            type = "image/*"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }

    fun shareImageToApp(imageUri: Uri, context: Context, packageName: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        shareIntent.type = "image/*"
        shareIntent.setPackage(packageName)

        try {
            context.startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            context.displayToast("This app not found")
        } catch (ex: Exception) {
            context.displayToast(ex.message.toString())
        }
    }



    fun shareImage(context: Context, fileUri: Uri?, appName: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            when (appName != null) {
                (appName?.contains("Facebook") == true) -> {
                    intent.setPackage("com.facebook.katana")
                }
                (appName?.contains("WhatsApp") == true) -> {
                    intent.setPackage("com.whatsapp")
                }
                (appName?.contains("Instagram") == true) -> {
                    intent.setPackage("com.instagram.android")
                }
                (appName?.contains("Snapchat") == true) -> {
                    val intentComponent =
                        ComponentName(
                            "com.snapchat.android",
                            "com.snapchat.android.LandingPageActivity"
                        )
                    intent.component = intentComponent
                }
                (appName?.contains("Twitter") == true) -> {
                    intent.setPackage("com.twitter.android")
                }
                (appName?.contains("Pinterest") == true) -> {
                    intent.setPackage("com.pinterest")
                }
                (appName?.contains("Youtube") == true) -> {
                    intent.setPackage("com.google.android.youtube")
                }
                else -> {

                }
            }
            fileUri?.let {
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "I make this amazing photo with Photo Effect app,\n" + " https://play.google.com/store/apps/details?id=${context.packageName}"
                )
                intent.putExtra(Intent.EXTRA_STREAM, fileUri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.type = "image/*"
            }

            withContext(Dispatchers.Main) {
                try {
                    context.startActivity(intent)
                } catch (ex: ActivityNotFoundException) {
                    ex.printStackTrace()
                    context.displayToast("This app not found")
                } catch (e: Exception) {
                    context.displayToast(e.message.toString())
                }
            }
        }
    }


    fun loadImageUri(context: Context, mUri: String?): Uri? {
        var uri: Uri? = null
        getBitmap(context, mUri)?.let { bitmap ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream())
            context.let {
                val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "IMG_"+Calendar.getInstance().time, null)
                uri = Uri.parse(path)
            }
        }
        return uri
    }


    private fun getBitmap(context: Context, imagePath: String?): Bitmap? {
        var bmp: Bitmap? = null
        imagePath?.let {
            Glide.with(context)
                .asBitmap().load(imagePath)
                .listener(object : RequestListener<Bitmap?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        context.displayToast("Something went wrong")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        bmp = resource
                        return false
                    }

                }
                ).submit()
        }

        return bmp
    }


}


