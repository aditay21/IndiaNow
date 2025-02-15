package com.kaushaltechnology.india.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushaltechnology.india.dao.gnews.Article
import com.kaushaltechnology.india.utils.Utils
import com.kaushaltechnology.india.viewmodel.NewsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsItem(article: Article, pagerState: PagerState,viewModel: NewsViewModel) {




    Box(
        modifier = Modifier.fillMaxSize().padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(0.3f).padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE)),
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            article.urlToImage?.let { NewsCard(imageUrl = it) }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Box(modifier = Modifier.fillMaxWidth().weight(0.4f).padding(16.dp), contentAlignment = Alignment.TopCenter) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${article.id} ${Utils.replaceSpecialChar(article.title)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = Utils.replaceSpecialChar(article.description),
                        fontSize = 20.sp,
                        color = Color.Gray,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth().weight(0.2f), contentAlignment = Alignment.TopCenter) {
                SourceAndTimeView(
                     article,
                    pagerState,viewModel
                )
            }
        }
    }
}
