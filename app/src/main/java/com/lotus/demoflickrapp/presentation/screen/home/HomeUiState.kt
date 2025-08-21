package com.lotus.demoflickrapp.presentation.screen.home

import com.lotus.demoflickrapp.domain.model.Photo

data class HomeUiState(
    val photos: List<Photo> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val hasMorePages: Boolean = false
)