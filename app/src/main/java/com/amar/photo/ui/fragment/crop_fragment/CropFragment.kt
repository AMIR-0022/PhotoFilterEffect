package com.amar.photo.ui.fragment.crop_fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amar.photo.R
import com.amar.photo.databinding.FragmentCropBinding
import com.amar.photo.utils.AppConstants.TAG
import com.amar.photo.utils.AppUtils
import com.amar.photo.utils.imgGallery
import com.amar.photo.utils.selectiveImagePath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class CropFragment : Fragment() {

    private lateinit var binding: FragmentCropBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_crop, container, false)
        binding = FragmentCropBinding.bind(myView)

        binding.ivCropper.setAspectRatio(5, 6)
        binding.ivCropper.setImageUriAsync(
            Uri.fromFile(
                selectiveImagePath?.let { File(it) }
            )
        )

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        // crop image before destroy view
        cropSelectiveImage()
    }

    private fun cropSelectiveImage() {
        if (binding.ivCropper.croppedImage != null) {
            Log.d(TAG, "cropSelectiveImage: Cropping the Image")
            CoroutineScope(Dispatchers.Main).launch {
                imgGallery = AppUtils.resizeBitmap(binding.ivCropper.croppedImage, 1000, 1000)
            }
        }
    }


}