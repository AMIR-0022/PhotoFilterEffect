package com.amar.photo.ui.fragment.adjust_fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import coil.imageLoader
import coil.request.ImageRequest
import com.amar.photo.R
import com.amar.photo.custom_views.MaskableFrameLayout
import com.amar.photo.databinding.FragmentAdjustmentBinding
import com.amar.photo.porter_duff.PorterDuffEffects
import com.amar.photo.touch_listener.MultiTouchListener
import com.amar.photo.utils.Constants
import com.amar.photo.utils.SELECTIVE_ADJUSTMENT_ITEM
import com.amar.photo.utils.TransformUtil
import com.amar.photo.utils.downloadedFrame
import com.amar.photo.utils.imgGallery
import com.amar.photo.utils.isTemplateSelect
import com.amar.photo.utils.potterDuffMode
import dagger.hilt.android.AndroidEntryPoint
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVignetteFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.wysaid.nativePort.CGENativeLibrary

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class AdjustmentFragment : Fragment() {

    private lateinit var binding: FragmentAdjustmentBinding

    private val viewModel: AdjustVM by viewModels()

    private lateinit var adjustAdapter: AdjustAdapter

    private lateinit var multiTouchListener: MultiTouchListener

    private lateinit var gpuImage: GPUImage
    private lateinit var originalBitmap: Bitmap
    private var filteredBitmap = MutableLiveData<Bitmap>()

    // Filter values
    private var brightness = 0.0f
    private var contrast = 1.0f
    private var vignetteLow = 1.0f
    private var vignetteRange = 0.5f
    private var vignetteCenterX = 0.5f
    private var vignetteCenterY = 0.5f
    private var saturation = 1.0f
    private var hue = 0.0f
    private var sharpness = 0.0f
    private var blur = 0.0f

    // Current filter type
    private var currentFilter = FilterType.BRIGHTNESS

    // filter list
    private lateinit var filterInfo: ArrayList<Adjust>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_adjustment, container, false)
        binding = FragmentAdjustmentBinding.bind(myView)

        gpuImage = GPUImage(requireContext())
        multiTouchListener = MultiTouchListener(requireContext(), binding.maskedImageView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        SELECTIVE_ADJUSTMENT_ITEM = 0
    }

    private fun init() {
        setEffect()
        populateData()
        setClickListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setClickListeners() {
        binding.maskedImageView.setOnTouchListener(multiTouchListener)

        binding.seekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val info = filterInfo.find { it.type == currentFilter } ?: return
                val adjustedProgress = progress + info.min

                when (currentFilter) {
                    FilterType.BRIGHTNESS -> brightness = adjustedProgress / 50.0f         // range [-1, 1]
                    FilterType.CONTRAST -> contrast = adjustedProgress / 100.0f + 0.1f     // range [0.1, 2]
                    FilterType.SATURATION -> saturation = adjustedProgress / 100.0f        // range [0, 2]
                    FilterType.VIGNETTE -> vignetteLow = adjustedProgress / 100.0f         // range [0, 1]
                    FilterType.SHARPNESS -> sharpness = adjustedProgress / 100.0f          // range [0, 2]
                    FilterType.HUE -> hue = adjustedProgress / 50.0f                       // range [-1, 1]
                    FilterType.BLUR -> blur = adjustedProgress / 100.0f                    // range [0, 2]
                }
                viewModel.updateFilterProgress(currentFilter, adjustedProgress)
                applyFilters()
                binding.tvBegin.text = "$progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

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

        // --->>> set adjust adapter
        adjustAdapter = AdjustAdapter { adjust ->
            currentFilter = adjust.type
            setSeekBarInfo(adjust.min, adjust.max, adjust.progress)
        }
        binding.rvAdjustment.adapter = adjustAdapter
        setSeekBarInfo(-50, 50, 0)

        // --->>> observe adjust data
        viewModel.adjustData.observe(viewLifecycleOwner) { list ->
            filterInfo = list
            adjustAdapter.setData(list)
        }

    }

    private fun setSeekBarInfo(min: Int, max: Int, progress: Int) {
        binding.seekBar.max = max-min
        binding.seekBar.progress = progress-min // set progress after set min max

        binding.tvEnd.text = "${max-min}"
        binding.tvBegin.text = "${progress-min}"
    }

    fun getAdjustPercent(min: Int, max: Int, current: Int): Int {
        if (max == min) {
            throw IllegalArgumentException("Max and min cannot be the same value")
        }
        return ((current - min) / (max - min) * 100)
    }

    private fun applyFilters() {
        val ruleString = "@adjust brightness $brightness @adjust contrast $contrast @vignette $vignetteLow $vignetteRange $vignetteCenterX $vignetteCenterY @adjust saturation $saturation @adjust hue $hue @adjust sharpen $sharpness @adjust blur $blur"
        filteredBitmap.value = CGENativeLibrary.filterImage_MultipleEffects(imgGallery, ruleString, 1.0f)
    }

    private fun setAdjustFilter(value: Float) {
        when (SELECTIVE_ADJUSTMENT_ITEM) {
            0 -> {
                with(gpuImage) {
                    setFilter(GPUImageBrightnessFilter(value))
                    filteredBitmap.value = bitmapWithFilterApplied
                }
            }
            1 -> {
                with(gpuImage) {
                    setFilter(GPUImageContrastFilter(value))
                    filteredBitmap.value = bitmapWithFilterApplied
                }
            }
            2 -> {
                with(gpuImage) {
                    setFilter(GPUImageSaturationFilter(value))
                    filteredBitmap.value = bitmapWithFilterApplied
                }
            }
            3 -> {
                with(gpuImage) {
                    setFilter(GPUImageSharpenFilter(value))
                    filteredBitmap.value = bitmapWithFilterApplied
                }
            }
            4 -> {
                with(gpuImage) {
                    setFilter(GPUImageVignetteFilter(PointF(0.5f, 0.5f), floatArrayOf(0.0f, 0.0f, 0.0f), 0.0f, value))
                    filteredBitmap.value = bitmapWithFilterApplied
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