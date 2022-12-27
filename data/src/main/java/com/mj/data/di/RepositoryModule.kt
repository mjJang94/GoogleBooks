package com.mj.data.di

import com.mj.data.BookRepositoryImpl
import com.mj.data.datasource.BookRemoteDataSource
import com.mj.domain.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideBookRepository(
        bookRemoteDataSource: BookRemoteDataSource
    ): BookRepository = BookRepositoryImpl(bookRemoteDataSource)
}