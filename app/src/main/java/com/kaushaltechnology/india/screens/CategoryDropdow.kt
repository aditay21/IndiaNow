package com.kaushaltechnology.india.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryDropdown(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("General", "World", "Nation", "Business", "Technology", "Entertainment", "Sports", "Science", "Health")
    var expanded  by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(selectedCategory) }

    Box(modifier = Modifier.wrapContentHeight()) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)) // Saffron color
        ) {
            Text(text = selected.uppercase(), color = Color.White, fontSize = 16.sp)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(180.dp)
        // Set width to avoid text cutoff
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(text = category.toUpperCase(), fontSize = 14.sp, color = Color.Black)
                    },
                    onClick = {
                        selected = category
                        expanded = false
                        onCategorySelected(category)
                    }
                )
            }
        }
    }
}
