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

    @Query("""
    SELECT * FROM news 
    WHERE read = 0  -- Fetch only unread news
    AND published_at >= datetime('now', '-1 days')  -- Exclude news older than 2 days
    ORDER BY published_at DESC
""")
    suspend fun getDisplayArticls(): List<Article> // Fetch unread news sorted by published date

    @Query("UPDATE news SET read = 1 WHERE id = :articleId")
    suspend fun updateReadStatus(articleId: Long, read: Int = 1)
}
