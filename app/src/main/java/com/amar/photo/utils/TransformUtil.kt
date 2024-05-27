package com.amar.photo.utils

import com.amar.photo.touch_listener.MultiTouchListener.TransformInfo

object TransformUtil {
    private var transformInfo: TransformInfo? = null

    fun saveTransformInfo(info: TransformInfo) {
        transformInfo = info
    }

    fun getTransformInfo(): TransformInfo? {
        return transformInfo
    }
}