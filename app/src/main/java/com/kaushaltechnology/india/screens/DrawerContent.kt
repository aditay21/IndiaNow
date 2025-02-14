package com.kaushaltechnology.india.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawerContent(onCloseDrawer: () -> Unit) {
    // You can replace these actions with actual navigation logic if necessary
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(text = "Navigation", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        // Divider to separate the title from the content
        Divider(color = Color.Gray, thickness = 1.dp)

        // List of items in the drawer
        TextButton(onClick = onCloseDrawer) {
            Text("Home", fontSize = 18.sp)
        }
        TextButton(onClick = onCloseDrawer) {
            Text("Settings", fontSize = 18.sp)
        }
        TextButton(onClick = onCloseDrawer) {
            Text("About Us", fontSize = 18.sp)
        }
        TextButton(onClick = onCloseDrawer) {
            Text("Contact Us", fontSize = 18.sp)
        }

        // Add more options as needed
        Spacer(modifier = Modifier.weight(1f)) // Push everything to the top

        // Optionally add a footer or logout option
        TextButton(onClick = onCloseDrawer) {
            Text("Logout", fontSize = 18.sp, color = Color.Red)
        }
    }
}
