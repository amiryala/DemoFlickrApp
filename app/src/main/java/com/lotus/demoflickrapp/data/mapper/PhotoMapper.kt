package com.lotus.demoflickrapp.data.mapper

import com.lotus.demoflickrapp.data.remote.dto.PhotoDto
import com.lotus.demoflickrapp.data.remote.dto.PhotosPageDto
import com.lotus.demoflickrapp.domain.model.Photo
import com.lotus.demoflickrapp.domain.model.PhotoPage

fun PhotoDto.toDomain(): Photo {
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
        url = "https://live.staticflickr.com/$server/${id}_${secret}_w.jpg"
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