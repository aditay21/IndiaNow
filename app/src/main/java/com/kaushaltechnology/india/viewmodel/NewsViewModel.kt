package com.kaushaltechnology.india.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushaltechnology.india.dao.gnews.Article
import com.kaushaltechnology.india.dao.gnews.DisplayList
import com.kaushaltechnology.india.repositiory.NewsRepository
import com.kaushaltechnology.india.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    var isLoading = false


    // StateFlow for holding the list of articles
    private val _displayItemList = MutableStateFlow(
        DisplayList(status = "", totalResults = 0, articles = mutableListOf(), page = 0)
    )
    val newsStateFlow: StateFlow<DisplayList> = _displayItemList.asStateFlow()

    // StateFlow for handling errors
    private val _errorStateFlow = MutableStateFlow<String?>(null)
    val errorStateFlow: StateFlow<String?> = _errorStateFlow.asStateFlow()

    private var _showShimmerEffect = MutableStateFlow(true)
    val showShimmerEffect: StateFlow<Boolean> = _showShimmerEffect

    init {
        fetchNews()
    }

    // Fetch news and update state
    fun fetchNews() {
        viewModelScope.launch {
            _showShimmerEffect.value  = true
            delay(500)
            if (!checkInternetConnection()) {
                _errorStateFlow.value = "No Internet Connection"
                return@launch
            }
            _errorStateFlow.value = null

            repository.fetchNews().collect { result ->
                result.onSuccess { newsResponse ->

                    _displayItemList.update { currentList ->
                        currentList.copy(
                            status = newsResponse.status,
                            totalResults = newsResponse.totalResults,
                            articles = (currentList.articles + newsResponse.articles).toMutableList(),
                            page = newsResponse.page
                        )
                    }
                }.onFailure { exception ->
                    handleError(exception)
                }
            }
            _showShimmerEffect.value  = false
            Log.e("TAG","isloading false")

        }
    }

    // Fetch next page of news
    fun callNextPage(page: Int) {
        if (isLoading || page < _displayItemList.value.page) {
            return
        }

        if (!checkInternetConnection()) {
            _errorStateFlow.value = "No Internet Connection"
            return
        }

        _errorStateFlow.value = null
        isLoading = true
        viewModelScope.launch {
            repository.callNextPage(page).collect { result ->
                result.onSuccess { newsResponse ->
                    _displayItemList.update { currentList ->
                        currentList.copy(
                            status = newsResponse.status,
                            totalResults = newsResponse.totalResults,
                            articles = (currentList.articles + newsResponse.articles).toMutableList(),
                            page = newsResponse.page
                        )
                    }
                }.onFailure { exception ->
                    handleError(exception)
                }
            }
            isLoading = false
        }
    }

    // Mark an article as read
    fun markArticleAsRead(article: Article) {
        viewModelScope.launch {
            repository.markArticleAsRead(article)
        }
    }

    // Use networkHelper to check internet connection
    private fun checkInternetConnection(): Boolean {
        return networkHelper.isConnected()
    }

    // Handle errors by checking the type of exception
    private fun handleError(exception: Throwable) {
        when (exception) {
            is java.net.UnknownHostException -> {
                // No internet connection
                _errorStateFlow.value = "No Internet Connection"
            }
            is java.io.IOException -> {
                // Network issues, e.g., timeout
                _errorStateFlow.value = "Network Error: ${exception.message}"
            }
            is HttpException -> {
                // Server errors (4xx, 5xx responses)
                when (exception.code()) {
                    404 -> _errorStateFlow.value = "Error: Resource Not Found"
                    500 -> _errorStateFlow.value = "Error: Internal Server Error"
                    else -> _errorStateFlow.value = "Error: ${exception.message}"
                }
            }
            else -> {
                // General fallback error handling
                _errorStateFlow.value = "Unexpected Error: ${exception.message}"
            }
        }
    }
}
