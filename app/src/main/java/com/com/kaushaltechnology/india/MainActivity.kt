package com.com.kaushaltechnology.india

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.com.kaushaltechnology.india.screens.NewsScreen
import com.com.kaushaltechnology.india.ui.theme.IndiaNowTheme
import com.com.kaushaltechnology.india.viewmodel.NewsViewModel

class MainActivity : ComponentActivity() {

    private val newsViewModel: NewsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IndiaNowTheme {
                NewsScreen(newsViewModel)

            }
        }
    }
}

