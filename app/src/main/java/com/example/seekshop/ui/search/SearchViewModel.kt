package com.example.seekshop.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    private val accumlatedProducts = mutableListOf<Product>()

    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    var pagingState by mutableStateOf(PagingState())
        private set


    suspend fun search(term: String, start: Int = 0): Result<List<Product>> {
        val locationId = locationRepository.selectedLocationId
        if (locationId.isNullOrEmpty()) {
            return Result.failure(IllegalStateException("Location not selected"))
        }

        return try {
            val token = authRepository.getAuthToken()
            val result = retrofitClient.fetchProduct(
                authToken = token,
                locationId = locationId,
                term = term,
                start = start,
            )
            val products = result.getOrThrow().data.map { it.toDomain() }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun loadMoreIfNeeded(term: String) {
        if (pagingState.isLoading || pagingState.endReached) return

        viewModelScope.launch {
            pagingState = pagingState.copy(isLoading = true)

            val result = search(term, start = pagingState.currentOffset)
            if (result.isSuccess) {
                val newProducts = result.getOrNull().orEmpty()
                accumlatedProducts += newProducts

                _uiState.value = if (accumlatedProducts.isEmpty()) {
                    SearchUiState.Empty
                } else {
                    SearchUiState.Success(accumlatedProducts.toList())
                }

                pagingState = pagingState.copy(
                    isLoading = false,
                    currentOffset = accumlatedProducts.size,
                    endReached = newProducts.isEmpty()
                )
            } else {
                _uiState.value = SearchUiState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
                pagingState = pagingState.copy(isLoading = false)
            }
        }
    }

    fun startSearch(term: String) {
        accumlatedProducts.clear()
        pagingState = PagingState()
        _uiState.value = SearchUiState.Loading
        loadMoreIfNeeded(term)
    }
}

sealed class SearchUiState {
    object Empty : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val products: List<Product>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}

data class PagingState (
    val isLoading: Boolean = false,
    val endReached: Boolean = false,
    val currentOffset: Int = 0
)