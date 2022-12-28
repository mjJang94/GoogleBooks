package com.mj.data

import com.mj.data.datasource.BookRemoteDataSource
import com.mj.data.mapper.mapperToBook
import com.mj.domain.model.Book
import com.mj.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val remoteDateSource: BookRemoteDataSource,
) : BookRepository {

    override suspend fun searchBook(query: String, currentIndex: Int): Flow<Book> = flow {
        remoteDateSource.requestBooksData(query, currentIndex).collect {
            emit(it.mapperToBook())
        }
    }
}