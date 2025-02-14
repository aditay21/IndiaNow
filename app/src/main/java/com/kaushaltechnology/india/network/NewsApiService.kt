package com.kaushaltechnology.india.network

import com.kaushaltechnology.india.dao.gnews.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("category") category: String = "general",
        @Query("apikey") apiKey: String = "f682c1e6044246d6f2c5eef3ec5bc83c",
        @Query("country") country: String = "in",
        @Query("lang") language: String = "en",
        @Query("max") max: Int = 25,
        @Query("page") page: Int = 1
    ): Response<NewsResponse>
}