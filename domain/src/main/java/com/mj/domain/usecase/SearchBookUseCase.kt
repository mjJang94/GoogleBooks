package com.mj.domain.usecase

import com.mj.domain.repository.BookRepository
import javax.inject.Inject

class SearchBookUseCase @Inject constructor(
    private val repository: BookRepository,
) {
    suspend fun searchBook(query: String, currentIndex: Int) = repository.searchBook(query, currentIndex)
}