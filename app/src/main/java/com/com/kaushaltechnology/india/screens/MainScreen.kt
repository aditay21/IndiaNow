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
import com.com.kaushaltechnology.india.screens.NewsCard
import com.com.kaushaltechnology.india.screens.SourceAndTimeView
import com.com.kaushaltechnology.india.viewmodel.NewsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(viewModel: NewsViewModel) {

    val newsResponse by viewModel.newsStateFlow.collectAsState()
    val errorState by viewModel.errorStateFlow.collectAsState()
    val isLoading = newsResponse == null // Adjust this based on your actual loading state

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val pagerState = rememberPagerState(pageCount = { newsResponse?.articles?.size ?: 1 })
    val scope = rememberCoroutineScope()

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
                // Show loading spinner if data is not yet available
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (errorState != null) {
                    Text(
                        text = "Error loading news",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                } else if (newsResponse?.articles.isNullOrEmpty()) {
                    Text("No news available", modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    // Vertical Pager
                    Box(modifier = Modifier.weight(1f)) {
                        VerticalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                // Divide into 40% for Card and 60% for Text
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
                                                newsResponse?.articles?.get(page)?.urlToImage?.let { NewsCard(imageUrl = it) }
                                            }
                                        }
                                    }

                                    // Text with 60% height
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(0.5f) // 60% height
                                            .padding(16.dp),
                                        contentAlignment = Alignment.TopCenter // Align top for title, bottom for description
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.spacedBy(8.dp) // Space between title and description
                                        ) {
                                            // Title Text (with ellipsis at the end)
                                            Text(text = newsResponse?.articles?.get(page)?.title?.let {
                                                Utils.replaceSpecialChar(
                                                    it
                                                )
                                            }
                                                ?: "Title not available",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                                maxLines = 3,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.fillMaxWidth()
                                            )

                                            // Description Text
                                            Text(
                                                text = newsResponse?.articles?.get(page)?.description?.let {
                                                    Utils.replaceSpecialChar(
                                                        it
                                                    )
                                                } ?: "Details are not available yet!!",
                                                fontSize = 20.sp,
                                                color = Color.Gray,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }

                                    //Text 10%
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp, 0.dp, 0.dp, 0.dp)
                                            .weight(0.2f),
                                        contentAlignment = Alignment.TopCenter
                                    ) {
                                        newsResponse?.articles?.get(page)
                                            ?.let {
                                                SourceAndTimeView(
                                                    source = it.source.name,
                                                    time = Utils.getTimeZone(it.publishedAt)
                                                )
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
}



// Drawer Content
@Composable
fun DrawerContent(onCloseDrawer: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Navigation", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Divider()
        TextButton(onClick = onCloseDrawer) { Text("Close Drawer") }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
//    MainScreen()
}
