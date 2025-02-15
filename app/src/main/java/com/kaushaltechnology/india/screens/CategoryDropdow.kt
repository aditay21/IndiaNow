package com.kaushaltechnology.india.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
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
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500)) // Saffron color
        ) {
            Text(text = selected, color = Color.White, fontSize = 16.sp)
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
                        Text(text = category, fontSize = 14.sp, color = Color.Black)
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
