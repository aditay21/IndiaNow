package com.kaushaltechnology.india.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun NoInterNetScreen() {
    Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp,0.dp,6.dp,56.dp), // Adjust based on your layout
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Red),
                elevation =  CardDefaults.cardElevation(8.dp)
            ) {
                Text(
                    text = "No Internet Connection",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White
                )
            }
    }
}