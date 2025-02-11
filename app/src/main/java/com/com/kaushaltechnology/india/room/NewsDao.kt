package com.com.kaushaltechnology.india.room

import androidx.room.Query
import com.com.kaushaltechnology.india.dao.gnews.NewsItem

@androidx.room.Dao
interface NewsDao {
    @androidx.room.Insert
    suspend fun insertAll(news: List<NewsItem>)

    @androidx.room.Query("SELECT * FROM news")
    suspend fun getAllNews(): List<NewsItem>

    @Query("SELECT * FROM news ORDER BY published_at DESC")
    suspend fun getAllNewsSorted(): List<NewsItem>


    @androidx.room.Query("DELETE FROM news")
    suspend fun deleteAllNews()

    @androidx.room.Query("SELECT * FROM news WHERE id = :newsId")
    suspend fun getNewsById(newsId: Int): NewsItem?

    @Query("SELECT * FROM news WHERE seen = 0")
    suspend fun getPendingNews(): List<NewsItem> // Fetch news where `seen = false`

    @Query("SELECT * FROM news WHERE seen = :seen ORDER BY published_at DESC")
    suspend fun getNewsBySeen(seen: Boolean): List<NewsItem>
}