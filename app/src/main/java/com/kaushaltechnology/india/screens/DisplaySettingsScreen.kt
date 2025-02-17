package com.kaushaltechnology.india.screens



import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Helper function to save preferences in SharedPreferences
fun saveDisplayPreference(context: Context, key: String, value: Boolean) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean(key, value).apply()
}

@Composable
fun DisplaySettingsScreen(navController: NavController) {


    val syncTheme = remember { mutableStateOf(false) }
    val lightModeEnabled = remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState) // Make content scrollable
    ) {
        Text("Display Settings", style = MaterialTheme.typography.displaySmall)
        Spacer(modifier = Modifier.height(8.dp))

        // Sync Theme with Mobile
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = syncTheme.value,
                onCheckedChange = {
                    syncTheme.value = it
                   // savePreference(context, "SyncTheme", it) // Save preference as Boolean
                }
            )
            Text(text = "Sync Theme with Mobile")
        }

        // Light Mode
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = lightModeEnabled.value,
                onCheckedChange = {
                    lightModeEnabled.value = it
                  //  savePreference(context, "LightMode", it) // Save preference as Boolean
                }
            )
            Text(text = "Light Mode")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Go back to the previous screen
        TextButton(onClick = { navController.popBackStack() }) {
            Text(text = "Back to Drawer")
        }
    }
}
