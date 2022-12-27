package com.mj.data.di

import com.mj.data.datasource.BookRemoteDataSource
import com.mj.data.datasource.BookRemoteDateSourceImpl
import com.mj.data.remote.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class BookDataModule {

    @Provides
    @Singleton
    fun searchBook(api: ApiInterface): BookRemoteDataSource = BookRemoteDateSourceImpl(api)
}