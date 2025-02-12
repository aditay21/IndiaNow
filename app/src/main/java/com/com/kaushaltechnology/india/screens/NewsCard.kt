package com.com.kaushaltechnology.india.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.com.kaushaltechnology.`in`.R

@Composable
fun NewsCard(imageUrl: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6200EE)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Height of the card
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Load image from URL using Coil
            Log.e("TAG","imageUrl $imageUrl")
            GlideImage(imageUrl, R.drawable.no_camera)
        }
    }
}
