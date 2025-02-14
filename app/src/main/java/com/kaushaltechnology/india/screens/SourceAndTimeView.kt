package com.kaushaltechnology.india.screens


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushaltechnology.india.dao.gnews.Article
import com.kaushaltechnology.india.utils.TimeUtils
import com.kaushaltechnology.india.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SourceAndTimeView(article: Article, pagerState: PagerState) {
    var isButtonEnabled by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val source = article.source.name
    val time = TimeUtils.formatDateTime(article.publishedAt)
    val newsUrl = article.url
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side: Source and Time Text
        Column(
            modifier = Modifier
                .padding(16.dp, 1.dp, 16.dp, 16.dp)
                .weight(6f),  // Takes available space on the left
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Source: $source",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(1.dp)) // Space between Source and Time

            Text(
                text = "Time: $time",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }

        // Right side: Next button
        Column(
            modifier = Modifier
                .padding(6.dp, 1.dp, 16.dp, 4.dp)
                .weight(1f), // Adjusting this to allow space for the button
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = {
                if (isButtonEnabled) {
                    isButtonEnabled = false
                    Utils.shareTheAPPAndNews(newsUrl, context)
                }
                coroutineScope.launch {
                    delay(1000) // 1 second delay
                    isButtonEnabled = true // Re-enable button
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "Share",
                    tint = if (isButtonEnabled) Color.Black else Color.Gray // Show disabled state
                )
            }

        }
        Column(
            modifier = Modifier
                .padding(4.dp, 1.dp, 16.dp, 16.dp)
                .weight(1f), // Adjusting this to allow space for the button
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = {
                coroutineScope.launch {
                    if (isButtonEnabled) {
                        isButtonEnabled = false
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }

                coroutineScope.launch {
                    delay(500) // 1 second delay
                    isButtonEnabled = true // Re-enable button
                }
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Page", tint = if (isButtonEnabled) Color.Black else Color.Gray )
            }

        }
    }
}
