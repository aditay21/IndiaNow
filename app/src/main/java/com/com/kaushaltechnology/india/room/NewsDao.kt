package com.com.kaushaltechnology.india.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.com.kaushaltechnology.india.dao.gnews.Article

@Dao
interface NewsDao {

    @Insert
    suspend fun insertAll(news: List<Article>)

    @Query("SELECT * FROM news")
    suspend fun getAllNews(): List<Article>

    @Query("SELECT * FROM news ORDER BY published_at DESC")
    suspend fun getAllNewsSorted(): List<Article>

    @Query("DELETE FROM news")
    suspend fun deleteAllNews()

    @Query("SELECT * FROM news WHERE id = :newsId")
    suspend fun getNewsById(newsId: Long): Article?

    @Query("SELECT * FROM news WHERE seen = 0")
    suspend fun getPendingNews(): List<Article> // Fetch news where `seen = false`

    // Removed the `seen` parameter since it's already defined in the query
    @Query("SELECT * FROM news WHERE seen = 0 ORDER BY published_at DESC")
    suspend fun getNewsBySeen(): List<Article> // Fetch unread news sorted by published date
}
