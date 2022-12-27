package com.mj.domain.repository

import com.mj.domain.model.Book

interface BookRepository {
    fun searchBook(query: String): Book
}