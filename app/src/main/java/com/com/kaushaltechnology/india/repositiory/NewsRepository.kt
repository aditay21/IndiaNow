package com.com.kaushaltechnology.india.repositiory

import com.com.kaushaltechnology.india.dao.gnews.NewsItem
import com.com.kaushaltechnology.india.dao.gnews.NewsResponse
import com.com.kaushaltechnology.india.network.NewsApiService
import com.com.kaushaltechnology.india.room.NewsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao
) {
    // Fetch from API and save to Room
    fun fetchNews(): Flow<Response<NewsResponse>> = flow {
        val response = apiService.getNews(
            accessKey = "9a33592114cc520c89df18fd70ad2917",
            country = "in",
            language = "en"
        )
        if (response.isSuccessful) {
            response.body()?.newsList?.let { articles ->
                newsDao.deleteAllNews()  // Clear old data
                newsDao.insertAll(articles)  // Save new data
            }
        }
       // emit(response)
    }

    // Get news from Room
    fun getLocalNews(): Flow<List<NewsItem>> = flow {
        emit(newsDao.getAllNews())
    }
}
