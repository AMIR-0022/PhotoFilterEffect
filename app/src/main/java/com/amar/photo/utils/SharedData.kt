package com.amar.photo.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.amar.photo.touch_listener.MultiTouchListener


var selectiveImagePath: String? = null
var imgGallery: Bitmap? = null

var downloadedFrame: Drawable? = null
var downloadedFrameLink: String? = null
var isTemplateSelect = false

var potterDuffMode = 0

var SELECT_DASHBOARD_NAV_ITEM: Int = 1
var SELECTIVE_EDITOR_NAV_ITEM: Int = 0
var SELECTIVE_ADJUSTMENT_ITEM: Int = 0