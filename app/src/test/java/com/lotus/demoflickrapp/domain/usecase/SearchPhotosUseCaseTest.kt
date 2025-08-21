package com.lotus.demoflickrapp.domain.usecase

import com.lotus.demoflickrapp.domain.model.Photo
import com.lotus.demoflickrapp.domain.model.PhotoPage
import com.lotus.demoflickrapp.domain.repository.PhotoRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SearchPhotosUseCaseTest {
    
    private lateinit var repository: PhotoRepository
    private lateinit var useCase: SearchPhotosUseCase
    
    @Before
    fun setup() {
        repository = mock()
        useCase = SearchPhotosUseCase(repository)
    }
    
    @Test
    fun invokeWithBlankQueryCallsGetRecentPhotos() = runTest {
        // Given
        val page = 1
        val mockPhotoPage = PhotoPage(
            photos = listOf(
                Photo(
                    id = "1",
                    owner = "owner1",
                    secret = "secret1",
                    server = "server1",
                    farm = 1,
                    title = "Recent Photo",
                    isPublic = 1,
                    isFriend = 0,
                    isFamily = 0,
                    url = "https://example.com/photo1.jpg"
                )
            ),
            page = 1,
            pages = 10,
            perPage = 30,
            total = 300
        )
        
        whenever(repository.getRecentPhotos(page)).thenReturn(Result.success(mockPhotoPage))
        
        // When
        val result = useCase("", page)
        
        // Then
        verify(repository).getRecentPhotos(page)
        assertTrue(result.isSuccess)
        assertEquals(mockPhotoPage, result.getOrNull())
    }
    
    @Test
    fun invokeWithWhitespaceQueryCallsGetRecentPhotos() = runTest {
        // Given
        val page = 1
        val mockPhotoPage = PhotoPage(
            photos = emptyList(),
            page = 1,
            pages = 1,
            perPage = 30,
            total = 0
        )
        
        whenever(repository.getRecentPhotos(page)).thenReturn(Result.success(mockPhotoPage))
        
        // When
        val result = useCase("   ", page)
        
        // Then
        verify(repository).getRecentPhotos(page)
        assertTrue(result.isSuccess)
    }
    
    @Test
    fun invokeWithNonBlankQueryCallsSearchPhotos() = runTest {
        // Given
        val query = "nature"
        val page = 1
        val mockPhotoPage = PhotoPage(
            photos = listOf(
                Photo(
                    id = "2",
                    owner = "owner2",
                    secret = "secret2",
                    server = "server2",
                    farm = 2,
                    title = "Nature Photo",
                    isPublic = 1,
                    isFriend = 0,
                    isFamily = 0,
                    url = "https://example.com/photo2.jpg"
                )
            ),
            page = 1,
            pages = 5,
            perPage = 30,
            total = 150
        )
        
        whenever(repository.searchPhotos(query, page)).thenReturn(Result.success(mockPhotoPage))
        
        // When
        val result = useCase(query, page)
        
        // Then
        verify(repository).searchPhotos(query, page)
        assertTrue(result.isSuccess)
        assertEquals(mockPhotoPage, result.getOrNull())
    }
    
    @Test
    fun invokeReturnsFailureWhenRepositoryReturnsFailure() = runTest {
        // Given
        val query = "test"
        val page = 1
        val exception = RuntimeException("Network error")
        
        whenever(repository.searchPhotos(query, page)).thenReturn(Result.failure(exception))
        
        // When
        val result = useCase(query, page)
        
        // Then
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}