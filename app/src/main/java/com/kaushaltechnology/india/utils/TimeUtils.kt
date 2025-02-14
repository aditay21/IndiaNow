package com.kaushaltechnology.india.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

object TimeUtils {

    private const val TAG = "TimeUtils"

    fun formatDateTime(timestamp: String): String {
        return try {
            // Step 1: Parse timestamp (YYYY-MM-DD HH:mm:ss)
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Assuming stored time is in UTC
            val date = inputFormat.parse(timestamp) ?: return timestamp

            // Step 2: Convert to device local time
            val localTimeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            localTimeFormat.timeZone = TimeZone.getDefault() // Convert to device timezone
            val formattedDate = localTimeFormat.format(date)

            // Step 3: Get today's and yesterday's date for comparison
            val calendar = Calendar.getInstance()
            val today = localTimeFormat.format(calendar.time)

            calendar.add(Calendar.DAY_OF_YEAR, -1) // Move to yesterday
            val yesterday = localTimeFormat.format(calendar.time)

            // Step 4: Return proper format
            when (formattedDate) {
                today -> "Today"
                yesterday -> "Yesterday"
                else -> {
                    val outputFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
                    outputFormat.format(date) // Format as "dd/MM"
                }
            }
        } catch (e: Exception) {
            timestamp // Return original if parsing fails
        }
    }
}
