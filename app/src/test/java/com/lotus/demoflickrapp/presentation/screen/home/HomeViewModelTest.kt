package com.lotus.demoflickrapp.presentation.screen.home

import app.cash.turbine.test
import com.lotus.demoflickrapp.domain.model.Photo
import com.lotus.demoflickrapp.domain.model.PhotoPage
import com.lotus.demoflickrapp.domain.usecase.GetRecentPhotosUseCase
import com.lotus.demoflickrapp.domain.usecase.SearchPhotosUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    
    private lateinit var getRecentPhotosUseCase: GetRecentPhotosUseCase
    private lateinit var searchPhotosUseCase: SearchPhotosUseCase
    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getRecentPhotosUseCase = mock()
        searchPhotosUseCase = mock()
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun initialStateLoadsRecentPhotosSuccessfully() = runTest {
        // Given
        val mockPhotoPage = PhotoPage(
            photos = listOf(createMockPhoto("1")),
            page = 1,
            pages = 5,
            perPage = 30,
            total = 150
        )
        
        whenever(getRecentPhotosUseCase(1)).thenReturn(Result.success(mockPhotoPage))
        whenever(searchPhotosUseCase("", 1)).thenReturn(Result.success(mockPhotoPage))
        
        // When
        viewModel = HomeViewModel(getRecentPhotosUseCase, searchPhotosUseCase)
        advanceUntilIdle()
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(1, state.photos.size)
            assertEquals("1", state.photos[0].id)
            assertEquals(1, state.currentPage)
            assertEquals(5, state.totalPages)
            assertTrue(state.hasMorePages)
            assertNull(state.error)
        }
    }
    
    @Test
    fun initialStateHandlesErrorWhenLoadingRecentPhotosFails() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        whenever(getRecentPhotosUseCase(1)).thenReturn(Result.failure(exception))
        whenever(searchPhotosUseCase("", 1)).thenReturn(Result.failure(exception))
        
        // When
        viewModel = HomeViewModel(getRecentPhotosUseCase, searchPhotosUseCase)
        advanceUntilIdle()
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.photos.isEmpty())
            assertEquals("Network error", state.error)
        }
    }
    
    @Test
    fun onSearchSubmitPerformsSearchWithQuery() = runTest {
        // Given
        val mockInitialPage = PhotoPage(
            photos = listOf(createMockPhoto("1")),
            page = 1,
            pages = 1,
            perPage = 30,
            total = 30
        )
        val mockSearchPage = PhotoPage(
            photos = listOf(createMockPhoto("2"), createMockPhoto("3")),
            page = 1,
            pages = 3,
            perPage = 30,
            total = 90
        )
        
        whenever(getRecentPhotosUseCase(1)).thenReturn(Result.success(mockInitialPage))
        whenever(searchPhotosUseCase("", 1)).thenReturn(Result.success(mockInitialPage))
        whenever(searchPhotosUseCase("test", 1)).thenReturn(Result.success(mockSearchPage))
        
        viewModel = HomeViewModel(getRecentPhotosUseCase, searchPhotosUseCase)
        advanceUntilIdle()
        
        // When
        viewModel.onSearchQueryChanged("test")
        advanceTimeBy(600)
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("test", state.searchQuery)
            assertEquals(2, state.photos.size)
            assertEquals("2", state.photos[0].id)
            assertEquals("3", state.photos[1].id)
            assertEquals(3, state.totalPages)
            assertTrue(state.hasMorePages)
        }
    }
    
    @Test
    fun loadMorePhotosAddsPhotosToExistingList() = runTest {
        // Given
        val mockPage1 = PhotoPage(
            photos = listOf(createMockPhoto("1")),
            page = 1,
            pages = 3,
            perPage = 1,
            total = 3
        )
        val mockPage2 = PhotoPage(
            photos = listOf(createMockPhoto("2")),
            page = 2,
            pages = 3,
            perPage = 1,
            total = 3
        )
        
        whenever(getRecentPhotosUseCase(1)).thenReturn(Result.success(mockPage1))
        whenever(searchPhotosUseCase("", 1)).thenReturn(Result.success(mockPage1))
        whenever(getRecentPhotosUseCase(2)).thenReturn(Result.success(mockPage2))
        
        viewModel = HomeViewModel(getRecentPhotosUseCase, searchPhotosUseCase)
        advanceUntilIdle()
        
        // When
        viewModel.loadMorePhotos()
        advanceUntilIdle()
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(2, state.photos.size)
            assertEquals("1", state.photos[0].id)
            assertEquals("2", state.photos[1].id)
            assertEquals(2, state.currentPage)
            assertTrue(state.hasMorePages)
            assertFalse(state.isLoadingMore)
        }
    }
    
    @Test
    fun loadMorePhotosWorksWithMultipleConsecutiveCalls() = runTest {
        // Given
        val mockPage1 = PhotoPage(
            photos = listOf(createMockPhoto("1")),
            page = 1,
            pages = 3,
            perPage = 1,
            total = 3
        )
        val mockPage2 = PhotoPage(
            photos = listOf(createMockPhoto("2")),
            page = 2,
            pages = 3,
            perPage = 1,
            total = 3
        )
        val mockPage3 = PhotoPage(
            photos = listOf(createMockPhoto("3")),
            page = 3,
            pages = 3,
            perPage = 1,
            total = 3
        )
        
        whenever(getRecentPhotosUseCase(1)).thenReturn(Result.success(mockPage1))
        whenever(searchPhotosUseCase("", 1)).thenReturn(Result.success(mockPage1))
        whenever(getRecentPhotosUseCase(2)).thenReturn(Result.success(mockPage2))
        whenever(getRecentPhotosUseCase(3)).thenReturn(Result.success(mockPage3))
        
        viewModel = HomeViewModel(getRecentPhotosUseCase, searchPhotosUseCase)
        advanceUntilIdle()
        
        // When
        viewModel.loadMorePhotos()
        viewModel.loadMorePhotos()
        advanceUntilIdle()
        
        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(3, state.photos.size)
            assertEquals(3, state.currentPage)
            assertFalse(state.isLoadingMore)
            assertFalse(state.hasMorePages)
        }
    }
    
    private fun createMockPhoto(id: String) = Photo(
        id = id,
        owner = "owner$id",
        secret = "secret$id",
        server = "server$id",
        farm = 1,
        title = "Photo $id",
        isPublic = 1,
        isFriend = 0,
        isFamily = 0,
        url = "https://example.com/photo$id.jpg"
    )
}