package com.lotus.demoflickrapp.data.remote.api

import com.lotus.demoflickrapp.data.remote.dto.PhotosResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    
    @GET("services/rest/")
    suspend fun getRecentPhotos(
        @Query("method") method: String = "flickr.photos.getRecent",
        @Query("api_key") apiKey: String = API_KEY,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1,
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): PhotosResponseDto
    
    @GET("services/rest/")
    suspend fun searchPhotos(
        @Query("method") method: String = "flickr.photos.search",
        @Query("api_key") apiKey: String = API_KEY,
        @Query("text") text: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1,
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): PhotosResponseDto
    
    companion object {
        const val BASE_URL = "https://www.flickr.com/"
        const val API_KEY = "a0222db495999c951dc33702500fdc4d"
    }
}