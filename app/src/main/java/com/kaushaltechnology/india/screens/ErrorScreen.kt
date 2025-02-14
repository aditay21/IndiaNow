package com.kaushaltechnology.india.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaushaltechnology.india.utils.AppError

// Main Error Screen Composable that combines both the message and the button
@Composable
fun ErrorScreen(errorCode: Int, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val errorMessage = AppError.getMessageFromCode(errorCode)  // Get the error message based on the exception
        ErrorMessage(errorMessage)
        Spacer(modifier = Modifier.height(8.dp))
        RetryButton(onRetry = onRetry)
    }
}

// Reusable Error Message Composable
@Composable
fun ErrorMessage(errorMessage: String) {
    Text(
        text = "Error: $errorMessage",
        color = Color.Red,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
}

// Reusable Retry Button Composable
@Composable
fun RetryButton(onRetry: () -> Unit) {
    Button(onClick = onRetry) {
        Text("Retry")
    }
}
