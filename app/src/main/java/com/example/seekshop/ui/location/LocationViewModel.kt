package com.example.seekshop.ui.location

import android.Manifest
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seekshop.network.api.RetrofitClient
import com.example.seekshop.network.mappers.toDomain
import com.example.seekshop.domain.model.Location
import com.example.seekshop.providers.LocationProvider
import com.example.seekshop.repository.AuthRepository
import com.example.seekshop.repository.LocationRepository
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
    private val locationProvider: LocationProvider,
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _locationState = MutableStateFlow<LocationState>(LocationState.Empty)
    val locationState: StateFlow<LocationState> = _locationState.asStateFlow()

    fun fetchUserLocation(zipCode: String) {
        viewModelScope.launch {
            _locationState.value = LocationState.Loading
            try {
                val locations = retrofitClient.fetchLocations(
                    authToken = authRepository.getAuthToken(),
                    zipCode = zipCode
                )
                val domainLocations = locations.getOrThrow().data.map { it.toDomain() }
                _locationState.value = LocationState.Success(domainLocations)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun fetchUserLocation(lat: String, lon: String) {
        viewModelScope.launch {
            try {
                val locations = retrofitClient.fetchLocations(
                    authToken = authRepository.getAuthToken(),
                    latLong = "$lat,$lon"
                )
                val domainLocations = locations.getOrThrow().data.map { it.toDomain() }
                _locationState.value = LocationState.Success(domainLocations)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun fetchNearbyStores() {
        viewModelScope.launch {
            _locationState.value = LocationState.Loading
            try {
                val (lat, lon) = locationProvider.getLatLon().getOrThrow()
                fetchUserLocation(lat, lon)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun saveLocation(location: Location) {
        locationRepository.selectedLocationId = location.id
    }

    private fun handleError(e: Exception) {
        _locationState.value = LocationState.Error(e.message ?: "Unknown error")
    }
}

sealed class LocationState {
    data object Empty : LocationState()
    data object Loading : LocationState()
    data class Success(val locations: List<Location>) : LocationState()
    data class Error(val message: String) : LocationState()
}