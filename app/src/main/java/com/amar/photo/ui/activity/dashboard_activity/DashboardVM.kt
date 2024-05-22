package com.amar.photo.ui.activity.dashboard_activity

import androidx.lifecycle.ViewModel
import com.amar.photo.R

class DashboardVM: ViewModel() {

    fun dashboardBottomNavItems(): ArrayList<Dashboard> {
        val list = ArrayList<Dashboard>()
        list.clear()

        list.add(Dashboard("Camera", R.drawable.ic_camera_active, R.drawable.ic_camera_inactive))
        list.add(Dashboard("Home", R.drawable.ic_home_active, R.drawable.ic_home_inactive))
        list.add(Dashboard("My Work", R.drawable.ic_work_active, R.drawable.ic_work_inactive))

        return list
    }

    fun editorBottomNavItems(): ArrayList<Dashboard> {
        val list = ArrayList<Dashboard>()
        list.clear()

        list.add(Dashboard("Crop", R.drawable.ic_crop_active, R.drawable.ic_crop_inactive))
        list.add(Dashboard("Effect", R.drawable.ic_effect_active, R.drawable.ic_effect_inactive))
        list.add(Dashboard("Filter", R.drawable.ic_adjust_active, R.drawable.ic_adjust_inactive))

        return list
    }

}