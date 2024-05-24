package com.amar.photo.ui.activity.photo_activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.amar.photo.R
import com.amar.photo.databinding.ActivityPhotoBinding
import com.amar.photo.ui.activity.share_activity.ShareActivity
import com.amar.photo.utils.Constants
import com.amar.photo.utils.displayToast

class PhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoBinding

    private var imagePath: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo)

        // --->>> get data from intent
        imagePath = intent.getStringExtra(Constants.KEY_IMG_PATH)
        init()

    }

    private fun init() {
        populateData()
        setClickListener()
    }

    private fun setClickListener() {
        binding.customAppBar.setOnBackIconClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.ivShare.setOnClickListener {
            val intent =  Intent(this, ShareActivity::class.java)
            intent.putExtra(Constants.KEY_IMG_PATH, imagePath)
            startActivity(intent)
        }
    }

    private fun populateData() {
        // --->> set data to image view
        imagePath?.let {
            binding.ivImage.setImageURI(it.toUri())
        } ?: run {
            displayToast("Something went wrong")
        }

    }


}