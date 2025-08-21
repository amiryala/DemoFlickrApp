package com.lotus.demoflickrapp.domain.usecase

import com.lotus.demoflickrapp.domain.model.PhotoPage
import com.lotus.demoflickrapp.domain.repository.PhotoRepository
import javax.inject.Inject

class GetRecentPhotosUseCase @Inject constructor(
    private val repository: PhotoRepository
) {
    suspend operator fun invoke(page: Int): Result<PhotoPage> {
        return repository.getRecentPhotos(page)
    }
}