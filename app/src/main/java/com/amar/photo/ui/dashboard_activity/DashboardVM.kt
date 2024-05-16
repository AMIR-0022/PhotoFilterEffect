package com.amar.photo.ui.dashboard_activity

import androidx.lifecycle.ViewModel
import com.amar.photo.R

class DashboardVM: ViewModel() {

    fun dashboardBottomNavItems(): ArrayList<Dashboard> {
        val list = ArrayList<Dashboard>()
        list.clear()

        list.add(Dashboard("Camera", R.drawable.ic_work_active, R.drawable.ic_camera_inactive))
        list.add(Dashboard("Home", R.drawable.ic_home_active, R.drawable.ic_home_inactive))
        list.add(Dashboard("My Work", R.drawable.ic_work_active, R.drawable.ic_work_inactive))

        return list
    }

}