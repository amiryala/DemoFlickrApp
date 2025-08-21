package com.lotus.demoflickrapp.di

import com.lotus.demoflickrapp.domain.repository.PhotoRepository
import com.lotus.demoflickrapp.domain.usecase.GetRecentPhotosUseCase
import com.lotus.demoflickrapp.domain.usecase.SearchPhotosUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    
    @Provides
    @ViewModelScoped
    fun provideGetRecentPhotosUseCase(
        repository: PhotoRepository
    ): GetRecentPhotosUseCase {
        return GetRecentPhotosUseCase(repository)
    }
    
    @Provides
    @ViewModelScoped
    fun provideSearchPhotosUseCase(
        repository: PhotoRepository
    ): SearchPhotosUseCase {
        return SearchPhotosUseCase(repository)
    }
}