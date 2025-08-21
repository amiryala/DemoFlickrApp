package com.lotus.demoflickrapp.data.repository

import com.lotus.demoflickrapp.data.remote.api.FlickrApi
import com.lotus.demoflickrapp.data.remote.dto.PhotoDto
import com.lotus.demoflickrapp.data.remote.dto.PhotosPageDto
import com.lotus.demoflickrapp.data.remote.dto.PhotosResponseDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PhotoRepositoryImplTest {
    
    private lateinit var api: FlickrApi
    private lateinit var repository: PhotoRepositoryImpl
    
    @Before
    fun setup() {
        api = mock()
        repository = PhotoRepositoryImpl(api)
    }
    
    @Test
    fun `getRecentPhotos returns success when API call succeeds`() = runBlocking {
        // Given
        val mockPhoto = PhotoDto(
            id = "1",
            owner = "owner1",
            secret = "secret1",
            server = "server1",
            farm = 1,
            title = "Test Photo",
            isPublic = 1,
            isFriend = 0,
            isFamily = 0
        )
        val mockResponse = PhotosResponseDto(
            photos = PhotosPageDto(
                page = 1,
                pages = 10,
                perPage = 30,
                total = "300",
                photo = listOf(mockPhoto)
            ),
            stat = "ok"
        )
        
        whenever(api.getRecentPhotos(page = 1)).thenReturn(mockResponse)
        
        // When
        val result = repository.getRecentPhotos(1)
        
        // Then
        assertTrue(result.isSuccess)
        result.getOrNull()?.let { photoPage ->
            assertEquals(1, photoPage.photos.size)
            assertEquals("1", photoPage.photos[0].id)
            assertEquals("Test Photo", photoPage.photos[0].title)
            assertEquals(1, photoPage.page)
            assertEquals(10, photoPage.pages)
        }
    }
    
    @Test
    fun `getRecentPhotos returns failure when API call fails`() = runBlocking {
        // Given
        val exception = RuntimeException("Network error")
        whenever(api.getRecentPhotos(page = 1)).thenThrow(exception)
        
        // When
        val result = repository.getRecentPhotos(1)
        
        // Then
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
    
    @Test
    fun `searchPhotos returns success when API call succeeds`() = runBlocking {
        // Given
        val query = "test"
        val mockPhoto = PhotoDto(
            id = "2",
            owner = "owner2",
            secret = "secret2",
            server = "server2",
            farm = 2,
            title = "Search Result",
            isPublic = 1,
            isFriend = 1,
            isFamily = 0
        )
        val mockResponse = PhotosResponseDto(
            photos = PhotosPageDto(
                page = 1,
                pages = 5,
                perPage = 30,
                total = "150",
                photo = listOf(mockPhoto)
            ),
            stat = "ok"
        )
        
        whenever(api.searchPhotos(text = query, page = 1)).thenReturn(mockResponse)
        
        // When
        val result = repository.searchPhotos(query, 1)
        
        // Then
        assertTrue(result.isSuccess)
        result.getOrNull()?.let { photoPage ->
            assertEquals(1, photoPage.photos.size)
            assertEquals("2", photoPage.photos[0].id)
            assertEquals("Search Result", photoPage.photos[0].title)
            assertTrue(photoPage.photos[0].url.contains("server2"))
            assertTrue(photoPage.photos[0].url.contains("secret2"))
        }
    }
    
    @Test
    fun `searchPhotos returns failure when API call fails`() = runBlocking {
        // Given
        val query = "test"
        val exception = RuntimeException("API error")
        whenever(api.searchPhotos(text = query, page = 1)).thenThrow(exception)
        
        // When
        val result = repository.searchPhotos(query, 1)
        
        // Then
        assertTrue(result.isFailure)
        assertEquals("API error", result.exceptionOrNull()?.message)
    }
}