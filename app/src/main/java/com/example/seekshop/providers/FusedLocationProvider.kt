package com.example.seekshop.providers

import android.Manifest
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class FusedLocationProvider @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationProvider {

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    override suspend fun getLatLon(): Result<Pair<String, String>> = suspendCancellableCoroutine { cont ->
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10_000L
        ).build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.locations.firstOrNull()
                if (location != null) {
                    cont.resume(Result.success(location.latitude.toString() to location.longitude.toString()))
                } else {
                    cont.resume(Result.failure(RuntimeException("Location unavailable")))
                }
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
    }
}