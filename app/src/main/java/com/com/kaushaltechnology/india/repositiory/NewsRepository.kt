package com.com.kaushaltechnology.india.repositiory

import com.com.kaushaltechnology.india.dao.gnews.Article
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

    // Fetch News from either the local database (if pending) or from the API
    fun fetchNews(): Flow<Response<NewsResponse>> = flow {
        // First check if there are any pending news items (i.e., seen = false)
        val pendingNews = newsDao.getDisplayArticls() // Fetch only the pending news (seen = false)

        if (pendingNews.isNotEmpty()) {
            // Emit the pending news from the database if any
            emit(Response.success(NewsResponse("",0 ,pendingNews)))
        } else {
            // Otherwise, fetch from the API
            val response = apiService.getTopHeadlines(
                category = "general",
                apiKey = "f682c1e6044246d6f2c5eef3ec5bc83c",
                country = "in",
                language = "en",
                max = 25,
                page = 1
            )
            if (response.isSuccessful) {
                response.body()?.articles?.let { articles ->
                    // Convert publishedAt format before saving
                    val formattedArticles = articles.map { article ->
                        article.copy(publishedAt = article.getFormattedPublishedAt())
                    }

                    newsDao.insertAll(formattedArticles)  // Save new data
                }
            }
            emit(response)
        }
    }

    // Get news from Room
    fun getLocalNews(): Flow<List<Article>> = flow {
        emit(newsDao.getAllNews())
    }

    suspend fun markArticleAsRead(article: Article) {
        article.id?.let { articleId ->
            newsDao.updateReadStatus(articleId, 1) // Update the read status in the DB
        }
    }

    // Check for pending news with seen = false

}
