package com.kaushaltechnology.india.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.com.kaushaltechnology.`in`.R
import com.kaushaltechnology.india.utils.AppError

// Main Error Screen Composable that combines the icon, message, and button
@Composable
fun ErrorScreen(errorCode: Int, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the error icon
        if (errorCode == AppError.NO_INTERNET.code) {
            Image(
                painter = painterResource(id = R.drawable.no_wifi), // Use an error image from resources
                contentDescription = "Error Icon",
                modifier = Modifier.height(48.dp),
            )
        }else {
            Image(
                painter = painterResource(id = R.drawable.icon_error), // Use an error image from resources
                contentDescription = "Error Icon",
                modifier = Modifier.height(48.dp),
            )
        }
        Spacer(modifier = Modifier.height(16.dp)) // Add spacing between icon and message

        val errorMessage = AppError.getMessageFromCode(errorCode) // Get error message
        ErrorMessage(errorMessage) // Display the error message

        Spacer(modifier = Modifier.height(16.dp)) // Add spacing between message and retry button

        RetryButton(onRetry = onRetry) // Display retry button
    }
}

@Composable
fun ErrorMessage(errorMessage: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)// Add padding for better centering

    ) {
        Text(
            text = errorMessage,
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center, // ✅ This centers the text inside Text
            modifier = Modifier.fillMaxWidth() // ✅ This allows textAlign to work properly
        )
    }
}





// Reusable Retry Button Composable
@Composable
fun RetryButton(onRetry: () -> Unit) {
    Button(onClick = onRetry) {
        Text("Retry")
    }
}
