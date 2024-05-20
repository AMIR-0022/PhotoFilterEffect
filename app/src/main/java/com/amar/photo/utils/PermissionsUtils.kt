package com.amar.photo.utils

import android.content.Context
import android.Manifest
import android.app.Activity
import com.amar.photo.R
import com.amar.photo.constants.AppConstants
import pub.devrel.easypermissions.EasyPermissions

object PermissionsUtils {

    fun hasPermission(context: Context): Boolean =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )

    fun requestPermission(activity: Activity) {
        EasyPermissions.requestPermissions(
            activity,
            activity.resources.getString(R.string.string_permission_request),
            AppConstants.SELECT_IMAGE_FROM_GALLERY_CODE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

}