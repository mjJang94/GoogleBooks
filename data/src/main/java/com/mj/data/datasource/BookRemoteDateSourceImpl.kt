package com.mj.data.datasource

import com.mj.data.remote.ApiInterface
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookRemoteDateSourceImpl @Inject constructor(private val api: ApiInterface): BookRemoteDataSource {
    override suspend fun requestBooksData(query: String, currentIndex: Long) = flow {
        emit(api.getBooks(query, currentIndex))
    }
}