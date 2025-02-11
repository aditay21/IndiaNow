package com.com.kaushaltechnology.india.dao.gnews

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("pagination") val pagination: Pagination,
    @SerializedName("data") val newsList: List<NewsItem>
)