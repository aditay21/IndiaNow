package com.com.kaushaltechnology.india.dao.gnews

import androidx.room.Entity

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news")
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,  // Add primary key with auto-generate
    @SerializedName("title") @ColumnInfo(name = "title") val title: String,
    @SerializedName("description") @ColumnInfo(name = "description") val description: String,
    @SerializedName("url") @ColumnInfo(name = "url") val url: String,
    @SerializedName("image") @ColumnInfo(name = "url_to_image") val urlToImage: String?,
    @SerializedName("publishedAt") @ColumnInfo(name = "published_at") val publishedAt: String,
    @Embedded val source: Source,
    @ColumnInfo(name = "seen") val seen: Boolean = false

)


