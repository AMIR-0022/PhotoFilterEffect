package com.amar.photo.ui.fragment.adjust_fragment

data class Adjust(
    var title: String = "",
    var min: Int = 0,
    var max: Int = 0,
    var progress: Int = 0,
    var type: FilterType = FilterType.BRIGHTNESS,
)
