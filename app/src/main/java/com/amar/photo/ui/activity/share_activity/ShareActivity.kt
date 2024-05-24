package com.amar.photo.ui.activity.share_activity

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.amar.photo.R
import com.amar.photo.databinding.ActivityShareBinding
import com.amar.photo.utils.AppUtils
import com.amar.photo.utils.Constants
import com.amar.photo.utils.displayToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ShareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShareBinding

    private val viewModel: ShareVM by viewModels()
    private lateinit var shareAdapter: ShareAdapter

    private var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share)

        init()

    }

    private fun init(){
        populateData()
        setClickListeners()
    }

    private fun setClickListeners() {

    }

    private fun populateData() {
        // --->>> get data from intent
        imagePath = intent.getStringExtra(Constants.KEY_IMG_PATH)
        // --->>> set data to image view
        imagePath?.let {
            binding.ivImage.setImageURI(it.toUri())
        } ?: run {
            displayToast("Something went wrong")
        }

        // --->> set Share-Adapter
        shareAdapter = ShareAdapter { share ->
            AppUtils.shareImage(this, imagePath!!.toUri(), share.appName)
        }
        binding.rvShare.adapter = shareAdapter
        // --->>> observe share adapter data
        shareAdapter.setData(viewModel.loadSocialShare())
    }



}