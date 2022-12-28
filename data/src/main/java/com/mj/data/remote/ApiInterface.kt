package com.mj.data.remote

import com.mj.data.model.BookResponse
import com.mj.data.remote.ApiClient.API_KEY
import com.mj.data.remote.ApiClient.BASE_URL
import com.mj.data.remote.ApiClient.DEFAULT_PRINT_TYPE
import com.mj.data.remote.ApiClient.DEFAULT_PROJECTION
import com.mj.data.remote.ApiClient.DEFAULT_RESULT_SIZE
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("/books/v1/volumes")
    suspend fun getBooks(
        @Query("q") query: String,
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResults: Int = DEFAULT_RESULT_SIZE,
        @Query("printType") printType: String = DEFAULT_PRINT_TYPE,
        @Query("projection") projection: String = DEFAULT_PROJECTION,
        @Query("key") key: String = API_KEY,
    ): BookResponse

    companion object {

        fun create(): ApiInterface {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val interceptor = Interceptor { chain ->
                with(chain) {
                    val newRequest = request().newBuilder()
                        .build()
                    proceed(newRequest)
                }
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}