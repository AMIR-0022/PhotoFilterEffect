package com.amar.photo.ui.fragment.filter_fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.net.Uri
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageAlphaBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorDodgeBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorMatrixFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageDarkenBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFalseColorFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGammaFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHalftoneFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHardLightBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLuminanceFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLuminanceThresholdFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageMonochromeFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageNormalBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImagePixelationFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImagePosterizeFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSolarizeFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSwirlFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToneCurveFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToonFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVibranceFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVignetteFilter
import java.io.InputStream
import javax.inject.Inject

class FilterImpRP @Inject constructor(private val context: Context) : FilterRP {
    override suspend fun prepareImagePreview(imageUri: Uri): Bitmap? {
        getInputStreamFromUri(imageUri)?.let {
            val bitmap = BitmapFactory.decodeStream(it)
            val width = context.resources.displayMetrics.widthPixels
            val height = ((bitmap.height) * width/bitmap.width)
            return  Bitmap.createScaledBitmap(bitmap, width, height, false)
        } ?: return null
    }

    private fun getInputStreamFromUri(uri: Uri): InputStream? {
        return context.contentResolver.openInputStream(uri)
    }

    override suspend fun getImageFilters(image: Bitmap): ArrayList<Filter> {
        val gpuImage = GPUImage(context).apply {
            setImage(image)
        }
        val imageFilter: ArrayList<Filter> = ArrayList()

        // REGION : Add ImageFilter

        // 1. Normal
        GPUImageFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Normal", filter, gpuImage.bitmapWithFilterApplied))
        }

        // 2. Hue
        GPUImageHueFilter(10.0f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Hue", filter, gpuImage.bitmapWithFilterApplied))
        }

        // 3. Saturation
        GPUImageSaturationFilter(1.5f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Saturate", filter, gpuImage.bitmapWithFilterApplied))
        }

        // 4. Contrast
        GPUImageContrastFilter(1.5f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Contrast", filter, gpuImage.bitmapWithFilterApplied))
        }

        // 5. Vibrance
        GPUImageVibranceFilter(0.5f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Vibrance Blend", filter, gpuImage.bitmapWithFilterApplied))
        }

        // 6. Gamma
        GPUImageGammaFilter(2.0f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Gamma", filter, gpuImage.bitmapWithFilterApplied))
        }

        // 7. Alpha Blend
        GPUImageAlphaBlendFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Alpha Blend", filter, gpuImage.bitmapWithFilterApplied))
        }

        // 8. Sepia
        GPUImageSepiaToneFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Sepia", filter, gpuImage.bitmapWithFilterApplied))
        }

        // 9. GrayScale
        GPUImageGrayscaleFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Grayscale", filter, gpuImage.bitmapWithFilterApplied))
        }


        // 10. Posterize
        GPUImagePosterizeFilter(10).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Posterize", filter, gpuImage.bitmapWithFilterApplied))
        }

        // 11. Monochrome
        GPUImageMonochromeFilter(1.0f, floatArrayOf(0.6f, 0.45f, 0.3f, 1.0f)).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Monochrome", filter, gpuImage.bitmapWithFilterApplied))
        }

        // 12. Vignette
        GPUImageVignetteFilter(
            PointF(0.5f, 0.5f),
            floatArrayOf(0.0f, 0.0f, 0.0f),
            0.3f,
            0.75f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Vignette", filter, gpuImage.bitmapWithFilterApplied))
        }

        // 13. Sharpen
        GPUImageSharpenFilter(4.0f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Sharpen", filter, gpuImage.bitmapWithFilterApplied))
        }

        // 14. Sketch
        GPUImageSketchFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Sketch", filter, gpuImage.bitmapWithFilterApplied))
        }

        // 15. Threshold
        GPUImageLuminanceThresholdFilter(0.2f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(Filter("Threshold", filter, gpuImage.bitmapWithFilterApplied))
        }

        sepiaFilter.also {
            gpuImage.setFilter(it)
            imageFilter.add(Filter("Sepia", it, gpuImage.bitmapWithFilterApplied))
        }

        // --->>> End Region

        return imageFilter
    }



    val sepiaFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            val sepia = GPUImageFilterGroup()
            sepia.addFilter(GPUImageSaturationFilter(0.8f))
            sepia.addFilter(GPUImageContrastFilter(1.2f))
            sepia.addFilter(GPUImageHueFilter(30f))
            filterGroup.addFilter(sepia)
            return filterGroup
        }

    val warmFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageContrastFilter(1.3f))
            filterGroup.addFilter(GPUImageBrightnessFilter(0.1f))
            filterGroup.addFilter(GPUImageHueFilter(15f))
            return filterGroup
        }

    val coolFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageContrastFilter(1.3f))
            filterGroup.addFilter(GPUImageBrightnessFilter(0.1f))
            filterGroup.addFilter(GPUImageHueFilter(-15f))
            return filterGroup
        }

    val vintageFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageToneCurveFilter().apply {
                setFromCurveFileInputStream(context.assets.open("vintage.acv"))
            })
            filterGroup.addFilter(GPUImageSaturationFilter(0.6f))
            return filterGroup
        }

    val crossProcessFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageToneCurveFilter().apply {
                setFromCurveFileInputStream(context.assets.open("crossprocess.acv"))
            })
            filterGroup.addFilter(GPUImageContrastFilter(1.2f))
            return filterGroup
        }

    val clarendonFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageContrastFilter(1.5f))
            filterGroup.addFilter(GPUImageSaturationFilter(1.2f))
            filterGroup.addFilter(GPUImageHueFilter(-10f))
            return filterGroup
        }

    val blackWhiteFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageGrayscaleFilter())
            filterGroup.addFilter(GPUImageContrastFilter(1.2f))
            return filterGroup
        }

    val vividFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageSaturationFilter(1.5.toFloat()))
            filterGroup.addFilter(GPUImageContrastFilter(1.5.toFloat()))
            filterGroup.addFilter(GPUImageHueFilter(-15f))
            return filterGroup
        }

    val solarizeVariantFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageHueFilter(180f))
            filterGroup.addFilter(GPUImageSolarizeFilter())
            return filterGroup
        }

    val solarusFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageFalseColorFilter())
            filterGroup.addFilter(GPUImageHueFilter(180f))
            return filterGroup
        }

    val pinkTintFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageContrastFilter(1.05f))
            filterGroup.addFilter(GPUImageSaturationFilter(0.9f))
            filterGroup.addFilter(createTintFilter(1.25f, 1.0f, 1.25f))
            return filterGroup
        }

    val lokiFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageContrastFilter(1.15f))
            filterGroup.addFilter(GPUImageSaturationFilter(1.1f))
            filterGroup.addFilter(createTintFilter(1.25f, 1.0f, 1.25f))
            return filterGroup
        }
    val lafaroFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageContrastFilter(1.35f))
            filterGroup.addFilter(GPUImageSaturationFilter(1.4f))
            filterGroup.addFilter(createTintFilter(1.16f, 1.0f, 1.25f))
            return filterGroup
        }

    val duotoneFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageFalseColorFilter())
            filterGroup.addFilter(GPUImageHueFilter(270f))
            return filterGroup
        }

    val duotone2Filter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageFalseColorFilter())
            filterGroup.addFilter(GPUImageHueFilter(120f))
            return filterGroup
        }

    val popartFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageFalseColorFilter())
            filterGroup.addFilter(GPUImageToonFilter())
            return filterGroup
        }

    val popart2Filter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageHalftoneFilter())
            filterGroup.addFilter(GPUImageFalseColorFilter())
            filterGroup.addFilter(GPUImageToonFilter())
            return filterGroup
        }

    val dramaticFilter: GPUImageFilterGroup
        get() {
            val amount = 1.1.toFloat()
            val amount2 = 0.5.toFloat()
            val dramaticFilter = GPUImageFilterGroup()
            dramaticFilter.addFilter(GPUImageGrayscaleFilter())
            dramaticFilter.addFilter(GPUImageContrastFilter(amount))
            return dramaticFilter
        }

    val rubrikFilter: GPUImageFilterGroup
        get() {
            val amount = 0.8.toFloat()
            val amount2 = 0.2.toFloat()
            val rubrikFilter = GPUImageFilterGroup()
            rubrikFilter.addFilter(GPUImageContrastFilter(amount))
            rubrikFilter.addFilter(GPUImageBrightnessFilter(amount2))
            return rubrikFilter
        }

    val eightFilter: GPUImageFilterGroup
        get() {
            val amount = 0.8.toFloat()
            val amount2 = (-0.2).toFloat()
            val eightFilter = GPUImageFilterGroup()
            eightFilter.addFilter(GPUImageSaturationFilter(amount))
            eightFilter.addFilter(GPUImageBrightnessFilter(amount2))
            return eightFilter
        }

    val fourFilter: GPUImageFilterGroup
        get() {
            val amount = 1.2.toFloat()
            val amount2 = (-0.4).toFloat()
            val fourFilter = GPUImageFilterGroup()
            fourFilter.addFilter(GPUImageSaturationFilter(amount))
            fourFilter.addFilter(GPUImageBrightnessFilter(amount2))
            fourFilter.addFilter(GPUImageContrastFilter(amount2))
            return fourFilter
        }

    val obsidianFilter: GPUImageFilterGroup
        get() {
            val amount = 1.4.toFloat()
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageGrayscaleFilter())
            filterGroup.addFilter(GPUImageContrastFilter(amount))
            return filterGroup
        }
    val vibrancyFilter: GPUImageFilterGroup
        get() {
            val amount = 1.1.toFloat()
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageContrastFilter(amount))
            filterGroup.addFilter(GPUImageVibranceFilter())
            return filterGroup
        }

    val caliFilter: GPUImageFilterGroup
        get() {
            val amt = 0.7.toFloat()
            val amt2 = 1.2.toFloat()
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageSaturationFilter(amt))
            filterGroup.addFilter(GPUImageContrastFilter(amt2))
            return filterGroup
        }

    val retroFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            val toneCurve = GPUImageToneCurveFilter()
            val rControlPoints = arrayOf(
                PointF(0.0f, 0.145f),
                PointF(0.063f, 0.153f), PointF(0.251f, 0.278f),
                PointF(0.573f, 0.776f), PointF(0.624f, 0.863f),
                PointF(0.682f, 0.922f), PointF(0.792f, 0.965f),
                PointF(1.0f, 1.0f)
            )
            val gControlPoints = arrayOf(
                PointF(0.0f, 0.0f),
                PointF(0.255f, 0.196f), PointF(0.447f, 0.576f),
                PointF(0.686f, 0.875f), PointF(1.0f, 1.0f)
            )
            val bControlPoints = arrayOf(
                PointF(0.0f, 0.137f),
                PointF(0.251f, 0.251f), PointF(0.345f, 0.376f),
                PointF(0.608f, 0.698f), PointF(0.890f, 0.91f),
                PointF(1.0f, 0.941f)
            )
            toneCurve.setRedControlPoints(rControlPoints)
            toneCurve.setGreenControlPoints(gControlPoints)
            toneCurve.setBlueControlPoints(bControlPoints)
            filterGroup.addFilter(toneCurve)
            val saturation = GPUImageSaturationFilter()
            saturation.setSaturation(0.8f)
            filterGroup.addFilter(saturation)
            return filterGroup
        }

    val retroFilter2: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            val toneCurve = GPUImageToneCurveFilter()
            val rControlPoints = arrayOf(
                PointF(0.0f, 0.145f),
                PointF(0.063f, 0.153f), PointF(0.251f, 0.278f),
                PointF(0.573f, 0.776f), PointF(0.624f, 0.863f),
                PointF(0.682f, 0.922f), PointF(0.792f, 0.965f),
                PointF(1.0f, 1.0f)
            )
            val gControlPoints = arrayOf(
                PointF(0.0f, 0.0f),
                PointF(0.255f, 0.196f), PointF(0.447f, 0.576f),
                PointF(0.686f, 0.875f), PointF(1.0f, 1.0f)
            )
            val bControlPoints = arrayOf(
                PointF(0.0f, 0.137f),
                PointF(0.251f, 0.251f), PointF(0.345f, 0.376f),
                PointF(0.608f, 0.698f), PointF(0.890f, 0.91f),
                PointF(1.0f, 0.941f)
            )
            toneCurve.setRedControlPoints(rControlPoints)
            toneCurve.setGreenControlPoints(gControlPoints)
            toneCurve.setBlueControlPoints(bControlPoints)
            filterGroup.addFilter(toneCurve)
            val saturation = GPUImageSaturationFilter()
            saturation.setSaturation(0.8f)
            filterGroup.addFilter(saturation)
            val contrast = GPUImageContrastFilter()
            contrast.setContrast(0.8f)
            filterGroup.addFilter(contrast)
            return filterGroup
        }

    val retroFilter3: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            val filter = GPUImageGrayscaleFilter()
            filterGroup.addFilter(filter)
            val toneCurve = GPUImageToneCurveFilter()
            val rControlPoints = arrayOf(
                PointF(0.0f, 0.145f),
                PointF(0.063f, 0.153f), PointF(0.251f, 0.278f),
                PointF(0.573f, 0.776f), PointF(0.624f, 0.863f),
                PointF(0.682f, 0.922f), PointF(0.792f, 0.965f),
                PointF(1.0f, 1.0f)
            )
            val gControlPoints = arrayOf(
                PointF(0.0f, 0.0f),
                PointF(0.255f, 0.196f), PointF(0.447f, 0.576f),
                PointF(0.686f, 0.875f), PointF(1.0f, 1.0f)
            )
            val bControlPoints = arrayOf(
                PointF(0.0f, 0.137f),
                PointF(0.251f, 0.251f), PointF(0.345f, 0.376f),
                PointF(0.608f, 0.698f), PointF(0.890f, 0.91f),
                PointF(1.0f, 0.941f)
            )
            toneCurve.setRedControlPoints(rControlPoints)
            toneCurve.setGreenControlPoints(gControlPoints)
            toneCurve.setBlueControlPoints(bControlPoints)
            filterGroup.addFilter(toneCurve)
            val saturation = GPUImageSaturationFilter()
            saturation.setSaturation(0.8f)
            filterGroup.addFilter(saturation)
            return filterGroup
        }

    val aestheticaFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            val amt = 0.4.toFloat()
            val exp_amt = 0.05.toFloat()
            filterGroup.addFilter(GPUImageContrastFilter(amt))
            filterGroup.addFilter(GPUImagePixelationFilter())
            return filterGroup
        }

    private val saturnFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            val amt = 0.4.toFloat()
            val brightness_amt = 0.15.toFloat()
            filterGroup.addFilter(GPUImageContrastFilter(amt))
            filterGroup.addFilter(GPUImageBrightnessFilter(brightness_amt))
            filterGroup.addFilter(GPUImageVibranceFilter(amt))
            return filterGroup
        }

    val lomoFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(vividFilter)
            filterGroup.addFilter(createVignette())
            return filterGroup
        }

    val retroVignette: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(retroFilter)
            filterGroup.addFilter(createVignette())
            return filterGroup
        }

    val retroVignette2: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(retroFilter2)
            filterGroup.addFilter(createVignette())
            return filterGroup
        }

    val grayscaleVignette: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(GPUImageGrayscaleFilter())
            filterGroup.addFilter(createVignette())
            return filterGroup
        }

    val swirlFilter: GPUImageFilter
        get() {
            val swirlFilter = GPUImageSwirlFilter()
            swirlFilter.setCenter(PointF(20.0f, 18.0f))
            swirlFilter.setRadius(15.3f)
            swirlFilter.setAngle(15.4f)
            return swirlFilter
        }

    val grayscaleVignette2: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            filterGroup.addFilter(dramaticFilter)
            filterGroup.addFilter(createVignette())
            return filterGroup
        }

    private val orbikFilter: GPUImageFilterGroup
        get() {
            val filterGroup = GPUImageFilterGroup()
            val amt = 0.4.toFloat()
            val exp_amt = 0.05.toFloat()
            filterGroup.addFilter(GPUImageContrastFilter(amt))
            filterGroup.addFilter(GPUImageSaturationFilter(amt))
            filterGroup.addFilter(GPUImageVibranceFilter(exp_amt))
            return filterGroup
        }


    private fun createVignette(): GPUImageVignetteFilter {
        val vignetteStart = 0.3f
        val vignetteEnd = 0.78f
        return GPUImageVignetteFilter(
            PointF(0.5f, 0.5f), floatArrayOf(0.0f, 0.0f, 0.0f),
            vignetteStart,
            vignetteEnd
        )
    }
    private fun createTintFilter(r: Float, g: Float, b: Float): GPUImageColorMatrixFilter {
        val filter = GPUImageColorMatrixFilter()
        val matrix = floatArrayOf(
            r, 0f, 0f, 0f, 0f,
            0f, g, 0f, 0f, 0f,
            0f, 0f, b, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
        filter.setColorMatrix(matrix)
        return filter
    }


}