package com.kaushaltechnology.india.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Helper function to save preferences in SharedPreferences
fun savePreference(context: Context, key: String, value: Boolean) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean(key, value).apply()
}

fun savePreference(context: Context, key: String, value: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString(key, value).apply()
}

@Composable
fun DrawerContent(navController: NavController?, onItemClick: (String) -> Unit) {
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)


    val selectedCountry = remember { mutableStateOf(sharedPreferences.getString("Country", "India") ?: "India") }
    val selectedLanguage = remember { mutableStateOf(sharedPreferences.getString("Language", "English") ?: "English") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // News Categories



        Spacer(modifier = Modifier.height(16.dp))
        Text("Preferences", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        // Country Preference
        Text("Choose Country:")
        listOf("India", "International").forEach { country ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedCountry.value == country,
                    onClick = {
                        selectedCountry.value = country
                        savePreference(context, "Country", country)
                    }
                )
                Text(text = country, modifier = Modifier.padding(start = 4.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Display Settings", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(4.dp))

        TextButton(onClick = { navController?.navigate("display_settings") }) {
            Text(text = "Go to Display Settings")
        }

        // Settings Options
        val settings = listOf(
            "Privacy Policy",
            "Rate Us",
            "Contact Us",
            "About Us",
            "App Version"
        )
        settings.forEach { setting ->
            TextButton(onClick = { onItemClick("Setting: $setting") }) {
                Text(text = setting)
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
