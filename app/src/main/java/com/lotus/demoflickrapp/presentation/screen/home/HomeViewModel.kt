package com.lotus.demoflickrapp.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lotus.demoflickrapp.domain.usecase.GetRecentPhotosUseCase
import com.lotus.demoflickrapp.domain.usecase.SearchPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecentPhotosUseCase: GetRecentPhotosUseCase,
    private val searchPhotosUseCase: SearchPhotosUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    // Separate StateFlow for search query to enable reactive debouncing
    private val searchQuery = MutableStateFlow("")
    
    init {
        loadRecentPhotos()
        observeSearchQuery()
    }
    
    private fun observeSearchQuery() {
        searchQuery
            .debounce(500) // Automatic debounce - cleaner than manual Job management
            .distinctUntilChanged() // Only react to actual changes
            .onEach { query ->
                performSearch(query)
            }
            .launchIn(viewModelScope)
    }
    
    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchQuery.value = query
    }
    
    fun onSearchSubmit() {
        // Immediate search without debounce
        viewModelScope.launch {
            performSearch(searchQuery.value)
        }
    }
    
    fun loadMorePhotos() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMorePages) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true, error = null) }
            
            val nextPage = _uiState.value.currentPage + 1
            val result = if (_uiState.value.searchQuery.isBlank()) {
                getRecentPhotosUseCase(nextPage)
            } else {
                searchPhotosUseCase(_uiState.value.searchQuery, nextPage)
            }
            
            result.fold(
                onSuccess = { photoPage ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            photos = currentState.photos + photoPage.photos,
                            isLoadingMore = false,
                            currentPage = nextPage,
                            totalPages = photoPage.pages,
                            hasMorePages = nextPage < photoPage.pages
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            error = exception.message ?: "An error occurred"
                        )
                    }
                }
            )
        }
    }
    
    fun retry() {
        viewModelScope.launch {
            performSearch(searchQuery.value)
        }
    }
    
    private fun loadRecentPhotos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            getRecentPhotosUseCase(1).fold(
                onSuccess = { photoPage ->
                    _uiState.update {
                        it.copy(
                            photos = photoPage.photos,
                            isLoading = false,
                            currentPage = 1,
                            totalPages = photoPage.pages,
                            hasMorePages = photoPage.pages > 1
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "An error occurred"
                        )
                    }
                }
            )
        }
    }
    
    private suspend fun performSearch(query: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        searchPhotosUseCase(query, 1).fold(
            onSuccess = { photoPage ->
                _uiState.update {
                    it.copy(
                        photos = photoPage.photos,
                        isLoading = false,
                        currentPage = 1,
                        totalPages = photoPage.pages,
                        hasMorePages = photoPage.pages > 1
                    )
                }
            },
            onFailure = { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "An error occurred"
                    )
                }
            }
        )
    }
}