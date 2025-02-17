package com.kaushaltechnology.india.repository

import android.util.Log
import com.kaushaltechnology.india.dao.gnews.Article
import com.kaushaltechnology.india.dao.gnews.NewsResponse
import com.kaushaltechnology.india.network.NewsApiService
import com.kaushaltechnology.india.room.NewsDao
import com.kaushaltechnology.india.utils.ApiError
import com.kaushaltechnology.india.utils.PreferenceHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao,
    private val sharedPreferences: PreferenceHelper
) {
    private var mPage = 1

    companion object {
        private const val API_KEY = "f682c1e6044246d6f2c5eef3ec5bc83c" // Move this to local.properties
        private const val CATEGORY = "general"
        private const val COUNTRY = "in"
        private const val LANGUAGE = "en"
        private const val MAX_RESULTS = 25
    }

    fun fetchOfflineData(): Flow<Result<NewsResponse>> = flow{
        val cachedNews = newsDao.getAllArticalsInNoInternet()
              emit(Result.success(NewsResponse("", 0, cachedNews, mPage)))
    }

    fun fetchNews(country :String): Flow<Result<NewsResponse>> = flow {
        try {
            val cachedNews = newsDao.getDisplayArticles(CATEGORY)
            if (cachedNews.size >= 2) {
                emit(Result.success(NewsResponse("", 0, cachedNews, mPage)))
                return@flow
            }

            val response = apiService.getTopHeadlines(
                category = CATEGORY,
                apiKey = API_KEY,
                country = country,
                language = LANGUAGE,
                max = MAX_RESULTS,
                page = mPage
            )

            if (response.isSuccessful) {
                response.body()?.articles?.let { articles ->
                    if (articles.isNotEmpty()) {
                        val formattedArticles = articles.map {
                            it.copy(publishedAt = it.getFormattedPublishedAt(), category =  CATEGORY)

                        }
                        insertUniqueArticles(formattedArticles)
                        mPage++  // Increment the page only after success
                    }
                }
                val updatedArticles = newsDao.getDisplayArticles(CATEGORY)
                emit(Result.success(NewsResponse("", 0, updatedArticles, mPage)))
            } else {
                emit(Result.failure(Exception(handleApiError(response.code()))))
            }
        } catch (e: Exception) {
            emit(Result.failure(Exception("Network error: ${e.localizedMessage}")))
        }
    }

    fun callNextPage(page: Int, category: String,country: String): Flow<Result<NewsResponse>> = flow {
        try {
            if (page < mPage) {
                Log.e("TAG", "Skipping API call, already fetched page $page")
                return@flow
            }

            val response = apiService.getTopHeadlines(
                category = category,
                apiKey = API_KEY,
                country =country,
                language = LANGUAGE,
                max = MAX_RESULTS,
                page = page
            )

            if (response.isSuccessful) {
                response.body()?.articles?.let { articles ->
                    if (articles.isNotEmpty()) {
                        val formattedArticles = articles.map {
                            it.copy(publishedAt = it.getFormattedPublishedAt(),category =  category) }
                        insertUniqueArticles(formattedArticles)
                        mPage = page + 1  // Update page after success
                    }
                }
                val updatedArticles = newsDao.getDisplayArticles(category)


                emit(Result.success(NewsResponse("", 0, updatedArticles, mPage)))
            } else {
                emit(Result.failure(Exception(handleApiError(response.code()))))
            }
        } catch (e: Exception) {
            emit(Result.failure(Exception("Network error: ${e.localizedMessage}")))
        }
    }

    suspend fun markArticleAsRead(article: Article) {
        newsDao.updateReadStatus(article.id, true)
    }

    private suspend fun insertUniqueArticles(formattedArticles: List<Article>) {
        newsDao.insertAll(formattedArticles) // No need for extra filtering, let DB handle uniqueness
    }

    private fun handleApiError(code: Int): String {
        return ApiError.fromCode(code).message
    }

    fun resetPage() {
        mPage = 1
    }
}
