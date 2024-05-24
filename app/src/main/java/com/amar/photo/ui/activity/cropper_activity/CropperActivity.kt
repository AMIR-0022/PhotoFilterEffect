package com.amar.photo.ui.activity.cropper_activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.amar.photo.R
import com.amar.photo.databinding.ActivityCropperBinding
import com.amar.photo.ui.activity.editor_activity.EditorActivity
import com.amar.photo.utils.Constants
import com.amar.photo.utils.AppUtils
import com.amar.photo.utils.imgGallery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class CropperActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCropperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cropper)

        val path = intent.getStringExtra(Constants.KEY_PATH)

        binding.ivCropper.setAspectRatio(5, 6)
        binding.ivCropper.setImageUriAsync(
            Uri.fromFile(path?.let { File(it) })
        )

        binding.ivCropCancel.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.ivCropSave.setOnClickListener {
            if (binding.ivCropper.croppedImage != null) {
                Log.d(Constants.TAG, "onCropSelectiveImage: Cropping the Image")
                binding.pbCroppingImage.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.Main).launch {
                    imgGallery = AppUtils.resizeBitmap(binding.ivCropper.croppedImage, 1000, 1000)

                    binding.pbCroppingImage.visibility = View.GONE

                    startActivity(Intent(this@CropperActivity, EditorActivity::class.java))
                    finish()
                }

            }
        }

    }
}