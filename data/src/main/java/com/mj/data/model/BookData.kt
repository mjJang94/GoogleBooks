package com.mj.data.model

import com.google.gson.annotations.SerializedName

data class BookData(
    val id: String,
    @SerializedName("volumeInfo")
    val bookInfo: BookInfo,
) {
    data class BookInfo(
        val title: String,
        val authors: List<Authors>,
        val publishedDate: String,
        val imageLinks: ImageLink,
        val infoLink: String,
    ) {
        data class Authors(
            val name: String,
        )

        data class ImageLink(
            val smallThumbnail: String,
            val thumbnail: String,
        )
    }
}
