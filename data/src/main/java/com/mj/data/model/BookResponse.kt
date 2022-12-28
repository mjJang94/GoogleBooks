package com.mj.data.model

data class BookResponse(
    val totalItems: Int,
    val items: List<BookData>?
)
