package com.mj.data

import com.mj.data.mapper.mapperToBook
import com.mj.data.datasource.BookRemoteDataSource
import com.mj.domain.model.Book
import com.mj.domain.repository.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val remoteDateSource: BookRemoteDataSource
): BookRepository {

    override fun searchBook(query: String): Book = remoteDateSource.requestBooksData(query).mapperToBook()
}