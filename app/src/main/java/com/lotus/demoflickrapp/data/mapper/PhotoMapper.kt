package com.lotus.demoflickrapp.data.mapper

import android.util.Log
import com.lotus.demoflickrapp.data.remote.dto.PhotoDto
import com.lotus.demoflickrapp.data.remote.dto.PhotosPageDto
import com.lotus.demoflickrapp.domain.model.Photo
import com.lotus.demoflickrapp.domain.model.PhotoPage

fun PhotoDto.toDomain(): Photo {
    // Using 'w' suffix for 400px width - good balance for grid display
    val imageUrl = "https://live.staticflickr.com/$server/${id}_${secret}_w.jpg"
    Log.d("PhotoMapper", "Constructed URL for photo $id: $imageUrl")
    Log.d("PhotoMapper", "Photo details - server: $server, secret: $secret, title: '$title'")
    
    return Photo(
        id = id,
        owner = owner,
        secret = secret,
        server = server,
        farm = farm,
        title = title,
        isPublic = isPublic,
        isFriend = isFriend,
        isFamily = isFamily,
        url = imageUrl
    )
}

fun PhotosPageDto.toDomain(): PhotoPage {
    return PhotoPage(
        photos = photo.map { it.toDomain() },
        page = page,
        pages = pages,
        perPage = perPage,
        total = total.toIntOrNull() ?: 0
    )
}