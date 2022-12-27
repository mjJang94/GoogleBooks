package com.mj.data.mapper

import com.mj.data.model.BookData
import com.mj.data.model.BookResponse
import com.mj.domain.model.Book
import com.mj.domain.model.BookItem

fun BookResponse.mapperToBook(): Book = with(this) {
    Book(
        totalCount = totalItems,
        items = items.formalize()
    )
}

private fun List<BookData>.formalize(): List<BookItem> =
    this.map { item ->
        BookItem(
            id = item.id,
            title = item.bookInfo.title ?: "",
            authors = item.bookInfo.authors?.joinToString(", ", "by ") ?: "",
            publishedDate = item.bookInfo.publishedDate ?: "",
            imageLinks = item.bookInfo.imageLinks?.smallThumbnail ?: "",
            detailLink = item.bookInfo.infoLink ?: ""
        )
    }