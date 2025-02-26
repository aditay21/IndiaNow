package com.com.kaushaltechnology.india.room


import androidx.room.Database
import androidx.room.RoomDatabase
import com.com.kaushaltechnology.india.dao.gnews.NewsItem

@Database(entities = [NewsItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}