package com.kaushaltechnology.india.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushaltechnology.india.dao.gnews.Article
import com.kaushaltechnology.india.dao.gnews.DisplayList
import com.kaushaltechnology.india.repositiory.NewsRepository
import com.kaushaltechnology.india.screens.fetchNewsForCategory
import com.kaushaltechnology.india.utils.ApiError
import com.kaushaltechnology.india.utils.AppError
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

    private val _displayItemList = MutableStateFlow(
        DisplayList(status = "", totalResults = 0, articles = mutableListOf(), page = 0)
    )
    val newsStateFlow: StateFlow<DisplayList> = _displayItemList.asStateFlow()

    private val _errorStateFlow = MutableStateFlow<Int?>(null)
    val errorStateFlow: StateFlow<Int?> = _errorStateFlow.asStateFlow()

    private var _showShimmerEffect = MutableStateFlow(true)
    val showShimmerEffect: StateFlow<Boolean> = _showShimmerEffect


    private val _selectedCategory = MutableStateFlow("General")
    val selectedCategory: StateFlow<String> = _selectedCategory

    fun updateCategory(category: String) {
        _selectedCategory.value = category
        fetchNewsForCategory(category)
    }

    init {
        fetchNews()
    }

    fun fetchNews() {
        viewModelScope.launch {
            _showShimmerEffect.value = true
            delay(500)
            if (!checkInternetConnection()) {
                _errorStateFlow.value = AppError.NO_INTERNET.code
                _showShimmerEffect.value = false
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
            _showShimmerEffect.value = false
            Log.e("TAG", "isLoading false")
        }
    }

    fun callNextPage(page: Int) {
        if (isLoading || page < _displayItemList.value.page) {
            return
        }

        if (!checkInternetConnection()) {
            _errorStateFlow.value = AppError.NO_INTERNET.code
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

    fun markArticleAsRead(article: Article) {
        viewModelScope.launch {
            repository.markArticleAsRead(article)
        }
    }

    private fun checkInternetConnection(): Boolean {
        return networkHelper.isConnected()
    }

    private fun handleError(exception: Throwable) {
        _errorStateFlow.value = AppError.getCodeFromException(exception)
    }
}





