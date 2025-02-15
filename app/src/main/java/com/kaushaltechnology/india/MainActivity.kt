package com.kaushaltechnology.india

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kaushaltechnology.india.screens.DisplaySettingsScreen
import com.kaushaltechnology.india.screens.MainScreen
import com.kaushaltechnology.india.ui.theme.IndiaNowTheme
import com.kaushaltechnology.india.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IndiaNowTheme {
                // Initialize the NavController
                val navController = rememberNavController()

                Scaffold(
                ) { innerPadding ->0
                    // Set up NavHost to manage navigation between screens
                    NavHost(
                        navController = navController,
                        startDestination = "home", // Initial screen
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            MainScreen(viewModel = newsViewModel,navController) // Your main screen
                        }
                        composable("display_settings") {
                            DisplaySettingsScreen(navController = navController) // Display settings screen
                        }
                    }
                }
            }
        }
    }
}
