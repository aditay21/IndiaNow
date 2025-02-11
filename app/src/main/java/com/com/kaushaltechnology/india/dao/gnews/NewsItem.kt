package com.com.kaushaltechnology.india.dao.gnews

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news")
data class NewsItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "author") @SerializedName("author") val author: String?,
    @ColumnInfo(name = "title") @SerializedName("title") val title: String,
    @ColumnInfo(name = "description") @SerializedName("description") val description: String,
    @ColumnInfo(name = "url") @SerializedName("url") val url: String,
    @ColumnInfo(name = "source") @SerializedName("source") val source: String,
    @ColumnInfo(name = "image") @SerializedName("image") val image: String?, @ColumnInfo(name = "category") @SerializedName("category") val category: String,
    @ColumnInfo(name = "language") @SerializedName("language") val language: String,
    @ColumnInfo(name = "country") @SerializedName("country") val country: String,
    @ColumnInfo(name = "published_at") @SerializedName("published_at") val publishedAt: String
)