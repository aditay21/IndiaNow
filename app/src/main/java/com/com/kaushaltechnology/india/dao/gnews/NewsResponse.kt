package com.com.kaushaltechnology.india.dao.gnews

import androidx.room.Entity
import com.google.gson.annotations.SerializedName


@Entity(tableName = "news")
data class NewsResponse(
   @SerializedName("status") val status: String,
   @SerializedName("totalResults") val totalResults: Int,
   @SerializedName("articles") val articles: List<Article>
)
