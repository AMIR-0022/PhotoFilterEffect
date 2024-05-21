package com.amar.photo.utils

import androidx.navigation.NavController
import androidx.navigation.NavOptions

object AppUtils {

    fun navigateToFragment(resId: Int, navController: NavController) {
        navController.navigate(resId, null,
            NavOptions.Builder()
                .setPopUpTo(navController.currentDestination!!.id, true)
                .build())
    }

}


