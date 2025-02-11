package com.com.kaushaltechnology.india.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SourceAndTimeView(source: String, time: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),

        verticalArrangement = Arrangement.Top,

    ) {
        // Source text
        Text(
            text = "Source: $source",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.width(8.dp)) // Space between Source and Time

        // Time text
        Text(
            text = "Time: $time",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Gray
        )
    }
}
