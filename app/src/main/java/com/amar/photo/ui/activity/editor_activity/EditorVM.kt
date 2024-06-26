package com.amar.photo.ui.activity.editor_activity

import androidx.lifecycle.ViewModel
import com.amar.photo.R

class EditorVM: ViewModel() {
    fun editorBottomNavItems(): ArrayList<Editor> {
        val list = ArrayList<Editor>()
        list.clear()

        list.add(Editor("Effect", R.drawable.ic_effect_active, R.drawable.ic_effect_inactive))
        list.add(Editor("Filter", R.drawable.ic_filter_active, R.drawable.ic_filter_inactive))
        list.add(Editor("Adjustment", R.drawable.ic_adjust_active, R.drawable.ic_adjust_inactive))

        return list
    }

}