package com.example.seekshop.ui.location

import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seekshop.network.RetrofitClient
import com.example.seekshop.network.model.Location
import com.example.seekshop.repository.AuthRepository
import com.example.seekshop.ui.AppPermissionChecker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val retrofitClient: RetrofitClient,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val permissionChecker: AppPermissionChecker
) : ViewModel() {
    private val _locationState = MutableStateFlow<LocationState>(LocationState.Empty)
    val locationState: StateFlow<LocationState> = _locationState.asStateFlow()

    private val _permissionRequestEvnet = MutableStateFlow<PermissionRequestEvent>(PermissionRequestEvent.Empty)
    val permissionRequestEvent : StateFlow<PermissionRequestEvent> = _permissionRequestEvnet.asStateFlow()

    fun requestLocationPermission() {
        if (!permissionChecker.checkLocationPermission()) {
            _permissionRequestEvnet.value = PermissionRequestEvent.Request
        } else {
            fetchLatLong()
        }
    }

    fun fetchUserLocation(zipCode: String) {
        viewModelScope.launch {
            _locationState.value = LocationState.Loading
            try {
                val locations = retrofitClient.fetchLocations(
                    authToken = authRepository.getAuthToken(),
                    zipCode = zipCode
                )
                _locationState.value = LocationState.Success(locations.getOrThrow().data)
            } catch (e: Exception) {
                _locationState.value = LocationState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchLatLong() {
        viewModelScope.launch {
            if(!permissionChecker.checkLocationPermission()) {
                _locationState.value = LocationState.Error("Location permission denied")
                return@launch
            }

            _locationState.value = LocationState.Loading

            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                10_000L
            ).build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    for (location in locationResult.locations) {
                        val lat = location.latitude
                        val long = location.longitude
                        Log.d("Location", "Lat: $lat, Long: $long")

                        viewModelScope.launch {
                            try {
                                val locations = retrofitClient.fetchLocations(
                                    authToken = authRepository.getAuthToken(),
                                    latLong = "$lat,$long"
                                )
                                _locationState.value =
                                    LocationState.Success(locations.getOrThrow().data)
                            } catch (e: Exception) {
                                _locationState.value =
                                    LocationState.Error(e.message ?: "Unknown error")
                            }
                        }

                        // Stop location updates as soon as we get the first location
                        fusedLocationProviderClient.removeLocationUpdates(this)
                        break
                    }
                }
            }
            try {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            } catch (e: Exception) {
                _locationState.value = LocationState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class LocationState {
    object Empty : LocationState()
    object Loading : LocationState()
    data class Success(val locations: List<Location>) : LocationState()
    data class Error(val message: String) : LocationState()
}

sealed class PermissionRequestEvent {
    object Request : PermissionRequestEvent()
    object Empty : PermissionRequestEvent()
}