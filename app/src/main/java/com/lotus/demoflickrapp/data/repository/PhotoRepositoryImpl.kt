package com.lotus.demoflickrapp.data.repository

import com.lotus.demoflickrapp.data.mapper.toDomain
import com.lotus.demoflickrapp.data.remote.api.FlickrApi
import com.lotus.demoflickrapp.domain.model.PhotoPage
import com.lotus.demoflickrapp.domain.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val api: FlickrApi
) : PhotoRepository {
    
    override suspend fun getRecentPhotos(page: Int): Result<PhotoPage> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getRecentPhotos(page = page)
                Result.success(response.photos.toDomain())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun searchPhotos(query: String, page: Int): Result<PhotoPage> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchPhotos(text = query, page = page)
                Result.success(response.photos.toDomain())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}