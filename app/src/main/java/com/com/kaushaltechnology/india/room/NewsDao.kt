package com.com.kaushaltechnology.india.room

import com.com.kaushaltechnology.india.dao.gnews.NewsItem

@androidx.room.Dao
interface NewsDao {
    @androidx.room.Insert
    suspend fun insertAll(news: List<NewsItem>)

    @androidx.room.Query("SELECT * FROM news")
    suspend fun getAllNews(): List<NewsItem>

    @androidx.room.Query("DELETE FROM news")
    suspend fun deleteAllNews()

    @androidx.room.Query("SELECT * FROM news WHERE id = :newsId")
    suspend fun getNewsById(newsId: Int): NewsItem?
}