package com.amar.photo.ui.fragment.effect_fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.amar.photo.R
import coil.imageLoader
import coil.request.ImageRequest
import com.amar.photo.custom_views.MaskableFrameLayout
import com.amar.photo.databinding.FragmentEffectBinding
import com.amar.photo.porter_duff.PorterDuffEffects
import com.amar.photo.touch_listener.MultiTouchListener
import com.amar.photo.ui.fragment.home_fragment.HomeVM
import com.amar.photo.utils.Constants.TAG
import com.amar.photo.utils.AppUtils
import com.amar.photo.utils.displayToast
import com.amar.photo.utils.downloadedFrame
import com.amar.photo.utils.imgGallery
import com.amar.photo.utils.isTemplateSelect
import com.amar.photo.utils.potterDuffMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EffectFragment : Fragment() {

    private lateinit var binding: FragmentEffectBinding

    private var filterImg = MutableLiveData<Bitmap>()

    private val viewModel: HomeVM by viewModels()
    private lateinit var effectAdapter: EffectAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_effect, container, false)
        binding = FragmentEffectBinding.bind(myView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        filterImg.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.Main).launch {
                binding.maskedImageView.setImageBitmap(it)
            }
        }

    }

    private fun init() {
        setEffect()
        populateData()
        setClickListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setClickListeners() {
        binding.maskedImageView.setOnTouchListener(
            MultiTouchListener(
                requireContext().applicationContext, binding.maskedImageView
            )
        )
    }

    private fun populateData() {
        // --->>> set thumb-effect adapter-data
        effectAdapter = EffectAdapter { effect ->
            CoroutineScope(Dispatchers.Main).launch {
                AppUtils.preDownloadImg(requireContext(), binding.pbEffectImages, effect) {
                    setEffect()
                }

            }
        }
        binding.rvEffect.adapter = effectAdapter
        // --->>> observe thumb-effect data
        viewModel.thumbImageUiState.observe(viewLifecycleOwner) {
            val dataState = it ?: return@observe

            binding.pbEffectImages.visibility =
                if(dataState.isLoading) View.VISIBLE else View.GONE

            dataState.data?.let { data ->
                effectAdapter.setData(data)
            } ?: run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    private fun setEffect() {
        CoroutineScope(Dispatchers.Main).launch {
            if (imgGallery != null && downloadedFrame != null) {

                isTemplateSelect = false
                setMaskEffect(imgGallery, downloadedFrame, downloadedFrame,
                    PorterDuffEffects.BlendModes[potterDuffMode] //9/10/11
                )
            } else {
                Toast.makeText(requireContext(), "Image Not Found", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setMaskEffect(maskedImageBitmap: Bitmap?, dstIn: Drawable?, blendMask: Drawable?, mode: PorterDuff.Mode?, ) {
        try {
            if (maskedImageBitmap != null) {
                binding.maskedImageView.setImageBitmap(maskedImageBitmap)
                setMask(dstIn!!, binding.maskDestinationLayout)     //cloud outer
                setMask(blendMask!!, binding.maskBlendLayout)       //inner color
                binding.maskBlendLayout.setPorterDuffXferMode(mode)
            }
        } catch (e: Exception) {
            Log.e(TAG, "onSetMaskEffectFailed: $e")
        }
    }

    private fun setMask(drawable: Drawable, maskableFrameLayout: MaskableFrameLayout) {
        try {
            val request = ImageRequest.Builder(requireContext())
                .data(drawable)
                .target { it ->
                    // Handle the result.
                    maskableFrameLayout.setMask(it)
                }
                .build()
            val disposable = requireContext().imageLoader.enqueue(request)

        } catch (e: Exception) {
            Log.e(TAG, "onSetMaskFailed---: $e")
        }
    }

}