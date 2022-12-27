package com.mj.domain.usecase

import com.mj.domain.model.Book
import com.mj.domain.repository.BookRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchBookUseCase @Inject constructor(
    private val repository: BookRepository,
) {
    suspend fun searchBook(query: String, currentIndex: Long) = repository.searchBook(query, currentIndex)
}