package com.example.seekshop.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seekshop.domain.model.Product
import com.example.seekshop.network.api.RetrofitClient
import com.example.seekshop.network.mappers.toDomain
import com.example.seekshop.repository.AuthRepository
import com.example.seekshop.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val retrofitClient: RetrofitClient,
    private val authRepository: AuthRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Empty)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun search(term: String) {
        val locationId = locationRepository.selectedLocationId
        if (locationId.isNullOrEmpty()) {
            _uiState.value = SearchUiState.Error("Location not selected")
            return
        }

        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            try {
                val token = authRepository.getAuthToken()
                val result = retrofitClient.fetchProduct(
                    authToken = token,
                    locationId = locationId,
                    term = term
                )
                val products = result.getOrThrow().data.map { it.toDomain() }
                _uiState.value = if (products.isEmpty()) SearchUiState.Empty else SearchUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class SearchUiState {
    object Empty : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val products: List<Product>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}