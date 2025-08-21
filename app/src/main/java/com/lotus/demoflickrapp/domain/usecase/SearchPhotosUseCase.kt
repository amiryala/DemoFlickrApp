package com.lotus.demoflickrapp.domain.usecase

import com.lotus.demoflickrapp.domain.model.PhotoPage
import com.lotus.demoflickrapp.domain.repository.PhotoRepository
import javax.inject.Inject

class SearchPhotosUseCase @Inject constructor(
    private val repository: PhotoRepository
) {
    suspend operator fun invoke(query: String, page: Int): Result<PhotoPage> {
        return if (query.isBlank()) {
            repository.getRecentPhotos(page)
        } else {
            repository.searchPhotos(query, page)
        }
    }
}