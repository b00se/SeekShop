package com.example.seekshop.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seekshop.network.RetrofitClient
import com.example.seekshop.network.model.Location
import com.example.seekshop.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val retrofitClient: RetrofitClient
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
                _locationState.value = LocationState.Success(locations.getOrThrow().data)
            } catch (e: Exception) {
                _locationState.value = LocationState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchLatLon() {
        viewModelScope.launch {
            try {
                // Simulate location fetching
                _locationState.value = LocationState.Success(listOf())
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