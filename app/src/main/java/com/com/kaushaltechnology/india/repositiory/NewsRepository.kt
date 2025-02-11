package com.com.kaushaltechnology.india.repositiory

import com.com.kaushaltechnology.india.dao.gnews.NewsItem
import com.com.kaushaltechnology.india.dao.gnews.NewsResponse
import com.com.kaushaltechnology.india.dao.gnews.Pagination
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

    // Fetch News from either the local database (if pending) or from the API
    fun fetchNews(): Flow<Response<NewsResponse>> = flow {
        // First check if there are any pending news items (i.e., seen = false)
        val pendingNews = newsDao.getPendingNews() // Fetch only the pending news (seen = false)

        if (pendingNews.isNotEmpty()) {
            // Emit the pending news from the database if any
            emit(Response.success(NewsResponse(Pagination(0,0,0,0) ,pendingNews)))
        } else {
            // Otherwise, fetch from the API
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
            emit(response)
        }
    }

    // Get news from Room
    fun getLocalNews(): Flow<List<NewsItem>> = flow {
        emit(newsDao.getAllNews())
    }

    // Check for pending news with seen = false
    private suspend fun getPendingNews(): List<NewsItem> {
        return newsDao.getNewsBySeen(false) // Implement a query for `seen = false`
    }
}
