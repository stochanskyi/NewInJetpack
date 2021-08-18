package com.mars.newinjetpack.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import com.mars.newinjetpack.presentation.camera.CameraActivity

fun Activity.requestCameraPermission(requestCode: Int) {
    requestPermissions(arrayOf(Manifest.permission.CAMERA), requestCode)
}

fun isCameraPermissionGranted(
    permissions: Array<out String>,
    grantResults: IntArray
): Boolean {
    if (permissions.isEmpty()) return false

    permissions.forEachIndexed { index, permission ->
        if (permission == Manifest.permission.CAMERA
            && grantResults[index] == PackageManager.PERMISSION_GRANTED) return true
    }

    return false
}