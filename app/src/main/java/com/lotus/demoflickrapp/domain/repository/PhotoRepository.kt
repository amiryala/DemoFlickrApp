package com.lotus.demoflickrapp.domain.repository

import com.lotus.demoflickrapp.domain.model.PhotoPage
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun getRecentPhotos(page: Int): Result<PhotoPage>
    suspend fun searchPhotos(query: String, page: Int): Result<PhotoPage>
}