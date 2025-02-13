package com.com.kaushaltechnology.india.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.com.kaushaltechnology.india.Utils
import com.com.kaushaltechnology.india.dao.gnews.Article
import com.com.kaushaltechnology.india.utils.TimeUtils
import com.com.kaushaltechnology.india.viewmodel.NewsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(viewModel: NewsViewModel) {
    val newsResponse by viewModel.newsStateFlow.collectAsState()
    val errorState by viewModel.errorStateFlow.collectAsState()
    val isLoading by remember { mutableStateOf(viewModel.isLoading) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val pagerState = rememberPagerState(pageCount = { newsResponse.articles.size })
    val scope = rememberCoroutineScope()

    // **✅ Observe page change and update read status**
    LaunchedEffect(pagerState.currentPage) {
        newsResponse.articles.getOrNull(pagerState.currentPage)?.let { article ->
            if (!article.read) {
                viewModel.markArticleAsRead(article)
            }
        }

        val lastItemIndex = newsResponse.articles.size - 3
        if (pagerState.currentPage >= lastItemIndex && !viewModel.isLoading) {
            Log.e("TAG", "LastItemIndex: $lastItemIndex, Page: ${newsResponse.page}")
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
                    .background(Color.White) // Set Background to White
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
                    isLoading -> {
                        // Show loading spinner
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    errorState != null -> {
                        // Show error message with retry button
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Error: $errorState",
                                color = Color.Red,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.fetchNews() }) {
                                Text("Retry")
                            }
                        }
                    }
                    newsResponse.articles.isEmpty() -> {
                        // Show message when there is no news
                        Text("No news available", modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    else -> {
                        if (pagerState.currentPage == 0 && newsResponse.articles.isNotEmpty()) {
                            viewModel.markArticleAsRead(newsResponse.articles[0])
                        }
                        // Vertical Pager
                        Box(modifier = Modifier.weight(1f)) {
                            VerticalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize()
                            ) { page ->
                                NewsItem(newsResponse.articles[page])
                            }
                        }
                    }
                }
            }
        }
    }
}

// **✅ Extracted NewsItem Composable**
@Composable
fun NewsItem(article: Article) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Card with 40% height
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .padding(16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF6200EE)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Height of the card
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        article.urlToImage?.let { NewsCard(imageUrl = it) }
                    }
                }
            }

            // Text with 60% height
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f) // 60% height
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Space between title and description
                ) {
                    // Title Text
                    Text(
                        text = "${article.id} ${Utils.replaceSpecialChar(article.title)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Description Text
                    Text(
                        text = Utils.replaceSpecialChar(article.description),
                        fontSize = 20.sp,
                        color = Color.Gray,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Source & Time
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 0.dp, 0.dp)
                    .weight(0.2f),
                contentAlignment = Alignment.TopCenter
            ) {
                SourceAndTimeView(
                    source = article.source.name,
                    time = TimeUtils.formatDateTime(article.publishedAt)
                )
            }
        }
    }
}

// Drawer Content
@Composable
fun DrawerContent(onCloseDrawer: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "Navigation", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        HorizontalDivider()
        TextButton(onClick = onCloseDrawer) { Text("Close Drawer") }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    // Preview
}
