package com.com.kaushaltechnology.india.network

import com.com.kaushaltechnology.india.dao.gnews.NewsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("news")
    suspend  fun getNews(
        @Query("access_key") accessKey: String,
        @Query("countries") country: String,
        @Query("languages") language: String
    ): Response<NewsResponse>
}