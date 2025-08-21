package com.lotus.demoflickrapp.domain.model

data class PhotoPage(
    val photos: List<Photo>,
    val page: Int,
    val pages: Int,
    val perPage: Int,
    val total: Int
)