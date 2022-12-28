package com.mj.data.datasource

import com.mj.data.model.BookResponse
import kotlinx.coroutines.flow.Flow

interface BookRemoteDataSource {
    suspend fun requestBooksData(query: String, currentIndex: Int): Flow<BookResponse>
}