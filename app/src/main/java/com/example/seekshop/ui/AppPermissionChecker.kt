package com.example.seekshop.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.seekshop.ui.location.IPermissionChecker
import javax.inject.Inject

class AppPermissionChecker @Inject constructor(private val context: Context): IPermissionChecker {
    override fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}