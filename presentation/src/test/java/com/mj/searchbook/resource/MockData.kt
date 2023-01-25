package com.mj.searchbook.resource

import com.mj.data.model.BookData
import com.mj.data.model.BookResponse

val nullResponseMock = BookResponse(
    totalItems = 5,
    items = mutableListOf<BookData>().apply {
        repeat(5){
            add(BookData(
                id = it.toString(),
                bookInfo = BookData.BookInfo(
                    title = null,
                    authors = null,
                    publishedDate = null,
                    imageLinks = null,
                    infoLink = null
                )
            ))
        }
    }
)