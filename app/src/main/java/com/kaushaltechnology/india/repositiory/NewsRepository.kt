package com.kaushaltechnology.india.repositiory

import android.util.Log
import com.kaushaltechnology.india.dao.gnews.Article
import com.kaushaltechnology.india.dao.gnews.NewsResponse
import com.kaushaltechnology.india.network.NewsApiService
import com.kaushaltechnology.india.room.NewsDao
import com.kaushaltechnology.india.utils.ApiError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao
) {
    private var mPage = 1

    companion object {
        private const val API_KEY = "f682c1e6044246d6f2c5eef3ec5bc83c"
        private const val CATEGORY = "general"
        private const val COUNTRY = "in"
        private const val LANGUAGE = "en"
        private const val MAX_RESULTS = 25
    }

    // Fetch News from either the local database or the API
    fun fetchNews(): Flow<Result<NewsResponse>> = flow {
        try {
            val pendingNews = newsDao.getDisplayArticls()
            if (pendingNews.size >= 2) {
                emit(Result.success(NewsResponse("", 0, pendingNews, mPage)))
            } else {
                val response = apiService.getTopHeadlines(
                    category = CATEGORY,
                    apiKey = API_KEY,
                    country = COUNTRY,
                    language = LANGUAGE,
                    max = MAX_RESULTS,
                    page = mPage
                )

                if (response.isSuccessful) {
                    mPage += 1
                    response.body()?.articles?.let { articles ->
                        val formattedArticles = articles.map { article ->
                            article.copy(publishedAt = article.getFormattedPublishedAt())
                        }
                        insertUniqueArticles(formattedArticles)
                    }
                    emit(Result.success(NewsResponse("", 0, newsDao.getDisplayArticls(), mPage)))
                } else {
                    emit(Result.failure(Exception(handleApiError(response.code()))))
                }
            }
        } catch (e: Exception) {
            emit(Result.failure(Exception("Network error: ${e.localizedMessage}")))
        }
    }

    // Fetch next page of news
    fun callNextPage(page: Int): Flow<Result<NewsResponse>> = flow {
        try {
            if (page < mPage) {
                Log.e("TAG", "Skipping API call, already fetched page $page")
                return@flow
            }

            val response = apiService.getTopHeadlines(
                category = CATEGORY,
                apiKey = API_KEY,
                country = COUNTRY,
                language = LANGUAGE,
                max = MAX_RESULTS,
                page = page
            )

            if (response.isSuccessful) {
                mPage = page + 1
                response.body()?.articles?.let { articles ->
                    val formattedArticles = articles.map { article ->
                        article.copy(publishedAt = article.getFormattedPublishedAt())
                    }
                    insertUniqueArticles(formattedArticles)
                }
                emit(Result.success(NewsResponse("", 0, newsDao.getDisplayArticls(), mPage)))
            } else {
                emit(Result.failure(Exception(handleApiError(response.code()))))
            }
        } catch (e: Exception) {
            emit(Result.failure(Exception("Network error: ${e.localizedMessage}")))
        }
    }

    // Mark an article as read
    suspend fun markArticleAsRead(article: Article) {
        newsDao.updateReadStatus(article.id, true)
    }

    // Function to insert unique articles
    private suspend fun insertUniqueArticles(formattedArticles: List<Article>) {
        /*val uniqueArticles = formattedArticles.filter { article ->
            val existingArticle = article.urlToImage?.let { newsDao.getArticleByImageUrl(it) }
            existingArticle == null
        }*/
        newsDao.insertAll(formattedArticles)
    }

    // Handle API errors
// Handle API errors using ApiError enum
    private fun handleApiError(code: Int): String {
        val apiError = ApiError.fromCode(code)
        return apiError.message
    }
}


