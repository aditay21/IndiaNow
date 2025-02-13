package com.com.kaushaltechnology.india.repositiory

import android.util.Log
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
    private var mPage = 1

    // Fetch News from either the local database or the API
    fun fetchNews(): Flow<Response<NewsResponse>> = flow {
        // Fetch pending news from local database (where seen = false)
        val pendingNews = newsDao.getDisplayArticls()

        if (pendingNews.size >= 2) {
            // Emit the pending news if we have more than 2 items
            emit(Response.success(NewsResponse("", 0, pendingNews,mPage)))
        } else {
            // Otherwise, fetch from the API if not enough data is available
            val response = apiService.getTopHeadlines(
                category = "general",
                apiKey = "f682c1e6044246d6f2c5eef3ec5bc83c",
                country = "in",
                language = "en",
                max = 25,
                page = mPage
            )
            if (response.isSuccessful) {
                mPage += 1
                response.body()?.articles?.let { articles ->

                    val formattedArticles = articles.map { article ->
                        article.copy(publishedAt = article.getFormattedPublishedAt())
                    }

                    // Save the new articles to the local database
                    insertUniqueArticles(formattedArticles)
                }
            }
            val articles = newsDao.getDisplayArticls()
            emit(Response.success(NewsResponse("", 0, articles,mPage)))
        }
    }

    // Fetch next page of news when reaching the last two items in the pager
    fun callNextPage(page:Int): Flow<Response<NewsResponse>> = flow {
        if (page<mPage){
            Log.e("TAG","NewsRepository $page Page  $mPage")
            return@flow
             }
        val response = apiService.getTopHeadlines(
            category = "general",
            apiKey = "f682c1e6044246d6f2c5eef3ec5bc83c",
            country = "in",
            language = "en",
            max = 25,
            page = page
        )

        if (response.isSuccessful) {
            mPage = page+1
            response.body()?.articles?.let { articles ->
                val formattedArticles = articles.map { article ->
                    article.copy(publishedAt = article.getFormattedPublishedAt())
                }

                // Save new articles to the database
                insertUniqueArticles(formattedArticles)
            }
        }

        // Fetch all pending news from the database (to emit the updated list)
        val pendingNews = newsDao.getDisplayArticls()
        if (pendingNews.isNotEmpty()) {
            emit(Response.success(NewsResponse("", 0, pendingNews, mPage)))
        }
    }

    // Mark an article as read
    suspend fun markArticleAsRead(article: Article) {
        article.id.let { articleId ->
            newsDao.updateReadStatus(articleId, true) // Update the read status
        }
    }

    // Function to insert new articles if not already in the database
    suspend fun insertUniqueArticles(formattedArticles: List<Article>) {
        // Filter out articles that already exist in the database based on image URL
       /* val uniqueArticles = formattedArticles.filter { article ->
            val existingArticle = article.urlToImage?.let { newsDao.getArticleByImageUrl(it) }
            existingArticle == null // Only include articles that don't already exist
        }*/

        // Insert the unique articles into the database
        newsDao.insertAll(formattedArticles)
    }
}
