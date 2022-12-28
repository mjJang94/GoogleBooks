package com.mj.domain.repository

import com.mj.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun searchBook(query: String, currentIndex: Int): Flow<Book>
}