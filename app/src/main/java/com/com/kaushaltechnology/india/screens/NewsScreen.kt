package com.com.kaushaltechnology.india.screens



import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.com.kaushaltechnology.india.viewmodel.NewsViewModel


@Composable
fun NewsScreen(viewModel: NewsViewModel) {

    val newsState by viewModel.newsStateFlow.collectAsState()
    val errorState by viewModel.errorStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchNews()
    }


}


