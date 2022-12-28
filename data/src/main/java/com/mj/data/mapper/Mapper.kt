package com.mj.data.mapper

import com.mj.data.model.BookData
import com.mj.data.model.BookResponse
import com.mj.domain.model.Book
import com.mj.domain.model.BookItem

fun BookResponse.mapperToBook(): Book = with(this) {
    Book(
        totalCount = totalItems,
        items = items?.formalize() ?: emptyList()
    )
}

private fun List<BookData>.formalize(): List<BookItem> =
    this.map { item ->
        BookItem(
            id = item.id,
            title = item.bookInfo.title.getOrBlank(),
            authors = item.bookInfo.authors?.joinToString(", ", "by ").getOrBlank(),
            publishedDate = item.bookInfo.publishedDate.getOrBlank(),
            imageLinks = item.bookInfo.imageLinks?.smallThumbnail.getOrBlank(),
            detailLink = item.bookInfo.infoLink.getOrBlank()
        )
    }

private fun String?.getOrBlank(): String = if (this.isNullOrEmpty()) "" else this