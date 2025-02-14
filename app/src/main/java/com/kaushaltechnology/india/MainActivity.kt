package com.kaushaltechnology.india

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.kaushaltechnology.india.screens.MainScreen
import com.kaushaltechnology.india.screens.DisplaySettingsScreen
import com.kaushaltechnology.india.screens.DrawerContent
import com.kaushaltechnology.india.ui.theme.IndiaNowTheme
import com.kaushaltechnology.india.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
                ) { innerPadding ->
                    // Set up NavHost to manage navigation between screens
                    NavHost(
                        navController = navController,
                        startDestination = "home", // Initial screen
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            MainScreen(viewModel = newsViewModel,navController) // Your main screen
                        }
                        composable("displaySettings") {
                            DisplaySettingsScreen(navController = navController) // Display settings screen
                        }
                    }
                }
            }
        }
    }
}
