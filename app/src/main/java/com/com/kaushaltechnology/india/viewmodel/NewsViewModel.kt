package com.com.kaushaltechnology.india.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.com.kaushaltechnology.india.dao.gnews.Article
import com.com.kaushaltechnology.india.dao.gnews.DisplayList
import com.com.kaushaltechnology.india.repositiory.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    var isLoading = false

    // StateFlow for holding the list of articles
    private val _displayItemList = MutableStateFlow<DisplayList>(DisplayList(status = "", totalResults = 0, articles = mutableListOf(), page = 0))
    val newsStateFlow: StateFlow<DisplayList> = _displayItemList

    // StateFlow for handling errors
    private val _errorStateFlow = MutableStateFlow<String?>(null)
    val errorStateFlow: StateFlow<String?> = _errorStateFlow

    init {
        fetchNews() // Fetch news on initialization
    }

    // Fetch news and update state
    private fun fetchNews() {
        viewModelScope.launch {
            repository.fetchNews().collect { response ->
                if (response.isSuccessful) {
                    response.body()?.let { newsResponse ->
                        // Update articles in _displayItemList
                        val updatedArticles = _displayItemList.value.articles + newsResponse.articles
                        _displayItemList.value = DisplayList(
                            status = newsResponse.status,
                            totalResults = newsResponse.totalResults,
                            articles = updatedArticles.toMutableList(), // Create a new mutable list
                            page = newsResponse.page
                        )
                    }
                } else {
                    _errorStateFlow.value = "Error: ${response.message()}"
                }
            }
        }
    }

    // Fetch next page of news
    fun callNextPage(page: Int) {
        if (isLoading) return  // Prevent multiple API calls

        isLoading = true
        viewModelScope.launch {
            repository.callNextPage(page).collect { response ->
                Log.e("TAG",  "Response ${response.body().toString()}")
                if (response.isSuccessful) {
                    response.body()?.let { newsResponse ->
                        // Add new articles for the next page
                        val updatedArticles = _displayItemList.value.articles + newsResponse.articles
                        _displayItemList.value = DisplayList(
                            status = newsResponse.status,
                            totalResults = newsResponse.totalResults,
                            articles = updatedArticles.toMutableList(),
                            page = newsResponse.page
                        )
                    }
                } else {
                    _errorStateFlow.value = "Error: ${response.message()}"
                }
            }
            isLoading = false
        }
    }

    // Mark an article as read
    fun markArticleAsRead(article: Article) {
        viewModelScope.launch {
            repository.markArticleAsRead(article)  // Update the read status in the DB
        }
    }
}
