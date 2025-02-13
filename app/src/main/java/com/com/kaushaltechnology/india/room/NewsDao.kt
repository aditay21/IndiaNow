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

    @Query(
        """
    SELECT DISTINCT * FROM news 
    WHERE read = 0  -- Fetch only unread news
    AND published_at >= datetime('now', '-1 days')  -- Exclude news older than 1 day
    ORDER BY published_at DESC
"""
    )
    suspend fun getDisplayArticls(): List<Article> // Fetch unread news sorted by published date


    @Query("UPDATE news SET read = :isRead WHERE id = :articleId")
    suspend fun updateReadStatus(articleId: Long, isRead: Boolean)

    @Query(
        """
    SELECT * FROM news 
    WHERE url_to_image = :imageUrl
"""
    )
    suspend fun getArticleByImageUrl(imageUrl: String): Article?

}
