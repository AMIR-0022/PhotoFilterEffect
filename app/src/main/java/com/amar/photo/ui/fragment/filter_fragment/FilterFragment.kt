package com.amar.photo.ui.fragment.filter_fragment

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
import coil.imageLoader
import coil.request.ImageRequest
import com.amar.photo.R
import com.amar.photo.custom_views.MaskableFrameLayout
import com.amar.photo.databinding.FragmentFilterBinding
import com.amar.photo.porter_duff.PorterDuffEffects
import com.amar.photo.touch_listener.MultiTouchListener
import com.amar.photo.touch_listener.MultiTouchListener.TransformInfo
import com.amar.photo.ui.fragment.effect_fragment.EffectAdapter
import com.amar.photo.utils.Constants
import com.amar.photo.utils.TransformUtil
import com.amar.photo.utils.displayToast
import com.amar.photo.utils.downloadedFrame
import com.amar.photo.utils.imgGallery
import com.amar.photo.utils.isTemplateSelect
import com.amar.photo.utils.potterDuffMode
import dagger.hilt.android.AndroidEntryPoint
import jp.co.cyberagent.android.gpuimage.GPUImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterFragment : Fragment() {

    private lateinit var binding: FragmentFilterBinding

    private val viewModel: FilterVM by viewModels()

    private lateinit var filterAdapter: FilterAdapter

    private lateinit var multiTouchListener: MultiTouchListener

    private lateinit var gpuImage: GPUImage
    private lateinit var originalBitmap: Bitmap
    private var filteredBitmap = MutableLiveData<Bitmap>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView =  inflater.inflate(R.layout.fragment_filter, container, false)
        binding = FragmentFilterBinding.bind(myView)

        gpuImage = GPUImage(requireContext())
        multiTouchListener = MultiTouchListener(requireContext(), binding.maskedImageView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        setEffect()
        populateData()
        setClickListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setClickListeners() {
        binding.maskedImageView.setOnTouchListener(multiTouchListener)
//        binding.maskedImageView.setOnTouchListener(
//            MultiTouchListener(
//                requireContext().applicationContext, binding.maskedImageView
//            )
//        )
    }

    private fun populateData() {
        // --->>> set transform-info to image
        TransformUtil.getTransformInfo()?.let { info ->
            multiTouchListener.setTransformInfo(binding.maskedImageView, info)
        }

        imgGallery?.let {
            filteredBitmap.value = it
            originalBitmap = it
            gpuImage.setImage(it)
        }

        filteredBitmap.observe(viewLifecycleOwner) {
            binding.maskedImageView.setImageBitmap(it)
        }

        // --->>> set filter adapter
        filterAdapter = FilterAdapter {
            Toast.makeText(requireContext(), "${it.name}", Toast.LENGTH_SHORT).show()
            with(it) {
                with(gpuImage) {
                    setFilter(filter)
                    filteredBitmap.value = bitmapWithFilterApplied
                }
            }
        }
        binding.rvFilter.adapter = filterAdapter
        // --->>> observe filter data
        viewModel.imageFiltersUiState.observe(viewLifecycleOwner) {
            val dataState = it ?: return@observe

            binding.pbFilterImages.visibility =
                if(dataState.isLoading) View.VISIBLE else View.GONE

            dataState.data?.let { data ->
                filterAdapter.setData(data)
            } ?: run {
                dataState.error?.let {  error ->
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
                    PorterDuffEffects.BlendModes[potterDuffMode]    // 9 - 10 - 11
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
            Log.e(Constants.TAG, "onSetMaskEffectFailed: $e")
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
            Log.e(Constants.TAG, "onSetMaskFailed---: $e")
        }
    }


}