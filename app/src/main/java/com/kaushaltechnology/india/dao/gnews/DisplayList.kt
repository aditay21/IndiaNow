package com.kaushaltechnology.india.dao.gnews

import com.google.gson.annotations.SerializedName

data class DisplayList(@SerializedName("status") val status: String,
                       @SerializedName("totalResults") val totalResults: Int,
                       @SerializedName("articles") val articles: MutableList<Article>,
                       val page: Int =0) {
}