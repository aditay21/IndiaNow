package com.kaushaltechnology.india.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kaushaltechnology.india.viewmodel.NewsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(viewModel: NewsViewModel) {
    val newsResponse by viewModel.newsStateFlow.collectAsState()
    val errorState by viewModel.errorStateFlow.collectAsState()
    val isLoading by remember { mutableStateOf(viewModel.isLoading) }
    val showShimmerEffect by viewModel.showShimmerEffect.collectAsState()
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // **✅ Add 1 extra page for error state**
    val pagerState = rememberPagerState(
        pageCount = { newsResponse.articles.size + if (errorState != null) 1 else 0 }
    )

    // **✅ Observe page change and update read status**
    LaunchedEffect(pagerState.currentPage) {
        newsResponse.articles.getOrNull(pagerState.currentPage)?.let { article ->
            if (!article.read) {
                viewModel.markArticleAsRead(article)
            }
        }

        val lastItemIndex = newsResponse.articles.size - 3
        if (pagerState.currentPage >= lastItemIndex && !viewModel.isLoading) {
            viewModel.callNextPage(newsResponse.page)
        }
    }

    // Drawer Layout
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                DrawerContent(onCloseDrawer = { scope.launch { drawerState.close() } })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("News App") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    showShimmerEffect -> {
                        // Show shimmer effect when loading
                        Log.e("TAG", "firstTimeLoading $showShimmerEffect")
                        ShimmerEffect()
                    }

                    else -> {
                        Box(modifier = Modifier.weight(1f)) {
                            VerticalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize()
                            ) { page ->
                                if (page < newsResponse.articles.size) {
                                    if (page == 0) {
                                        viewModel.markArticleAsRead(newsResponse.articles[0])
                                    }
                                    NewsItem(newsResponse.articles[page], pagerState)
                                } else if (errorState != null) {
                                    // **✅ Display error as last item**
                                    ErrorScreen(errorState!!) {
                                        viewModel.fetchNews()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


// Shimmer Effect Composable



