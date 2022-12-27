package com.mj.data.datasource

import com.mj.data.model.BookResponse

interface BookRemoteDataSource {
    fun requestBooksData(query: String): BookResponse
}