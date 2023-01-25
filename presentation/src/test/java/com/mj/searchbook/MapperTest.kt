package com.mj.searchbook

import com.mj.data.mapper.mapperToBook
import com.mj.data.model.BookResponse
import com.mj.domain.model.Book
import com.mj.domain.model.BookItem
import com.mj.searchbook.resource.nullResponseMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@ExperimentalCoroutinesApi
class MapperTest {

    @Test
    fun `getBooks() result items에 null이 있는 경우 mapper로 null처리가 되고, Book 객체로 변환되는지`() {

        val fakeResponse = nullResponseMock

        fakeResponse.items?.forEach { data ->
            data.also {
                assertNull(it.bookInfo.title)
                assertNull(it.bookInfo.authors)
                assertNull(it.bookInfo.publishedDate)
                assertNull(it.bookInfo.imageLinks)
                assertNull(it.bookInfo.infoLink)
            }
        }

        val fakeResponseWithMapper = fakeResponse.mapperToBook()

        fakeResponseWithMapper.items.forEach { data ->
            data.also {
                assertNotNull(it.id)
                assertNotNull(it.title)
                assertNotNull(it.authors)
                assertNotNull(it.publishedDate)
                assertNotNull(it.imageLinks)
                assertNotNull(it.detailLink)
            }
        }
        val expectedFakeBook = Book(
            totalCount = 5,
            items = mutableListOf<BookItem>().apply {
                repeat(5) {
                    add(BookItem(
                        id = it.toString(),
                        title = "",
                        authors = "",
                        publishedDate = "",
                        imageLinks = "",
                        detailLink = ""
                    )
                    )
                }
            }
        )
        assertEquals(expectedFakeBook, fakeResponseWithMapper)
    }

    @Test
    fun `getBooks() result items가 null인 경우 null처리가 되고 Book 객체로 변환되는지`() {

        val fakeResponse = BookResponse(
            totalItems = 0,
            items = null
        )

        assertNull(fakeResponse.items)

        val fakeResponseWithMapper = fakeResponse.mapperToBook()

        assertNotNull(fakeResponseWithMapper.items)
        assertEquals(Book(totalCount = 0, items = emptyList()), fakeResponseWithMapper)
    }
}