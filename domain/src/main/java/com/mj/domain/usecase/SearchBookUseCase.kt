package com.mj.domain.usecase

import com.mj.domain.repository.BookRepository
import javax.inject.Inject

class SearchBookUseCase @Inject constructor(
    private val repository: BookRepository,
) {
    fun searchBook(query: String) = repository.searchBook(query)
}