package com.com.kaushaltechnology.india.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.com.kaushaltechnology.india.dao.gnews.NewsResponse
import com.com.kaushaltechnology.india.repositiory.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    private val _newsStateFlow = MutableStateFlow<NewsResponse?>(null)
    val newsStateFlow: StateFlow<NewsResponse?> = _newsStateFlow

    private val _errorStateFlow = MutableStateFlow<String?>(null)
    val errorStateFlow: StateFlow<String?> = _errorStateFlow

    fun fetchNews() {
        viewModelScope.launch {
            repository.fetchNews().collect { response ->
                if (response.isSuccessful) {
                    _newsStateFlow.value = response.body()
                } else {
                    _errorStateFlow.value = "Error: ${response.message()}"
                }
            }
        }
    }
}