package com.mj.data.datasource

import com.mj.data.model.BookResponse
import com.mj.data.remote.ApiInterface
import javax.inject.Inject

class BookRemoteDateSourceImpl @Inject constructor(private val api: ApiInterface): BookRemoteDataSource {
    override fun requestBooksData(query: String): BookResponse = api.getBooks(query)
}