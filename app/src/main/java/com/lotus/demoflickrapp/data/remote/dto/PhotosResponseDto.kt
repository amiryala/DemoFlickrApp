package com.lotus.demoflickrapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PhotosResponseDto(
    @SerializedName("photos")
    val photos: PhotosPageDto,
    @SerializedName("stat")
    val stat: String
)

data class PhotosPageDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("perpage")
    val perPage: Int,
    @SerializedName("total")
    val total: String, // API returns this as a string
    @SerializedName("photo")
    val photo: List<PhotoDto>
)