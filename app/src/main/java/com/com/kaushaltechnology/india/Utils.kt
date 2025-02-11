package com.com.kaushaltechnology.india

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.util.Locale
import java.util.TimeZone


class Utils {

    companion object {

        val TAG = Companion::class.java.simpleName

        // Static-like method
        fun replaceSpecialChar(text: String): String {
            return text.replace("&#039;", "")
        }

        fun getTimeZone(timestamp: String): String {
            val countryTimeZone = TimeZone.getDefault().id
            Log.e(TAG, "Device Time Zone: $countryTimeZone")
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                convertToCountryTimeZone(timestamp, countryTimeZone)
            } else {
                timestamp
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun convertToCountryTimeZone(timestamp: String, countryTimeZone: String): String {
            // Step 1: Parse the given timestamp
            val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val utcTime = ZonedDateTime.parse(timestamp, formatter)

            // Step 2: Convert to the desired country's time zone
            val countryZoneId = ZoneId.of(countryTimeZone)
            val countryTime = utcTime.withZoneSameInstant(countryZoneId)

            // Step 3: Get the current date and compare it
            val now = ZonedDateTime.now(ZoneId.of(countryTimeZone))
            val todayStart = now.toLocalDate().atStartOfDay(now.zone) // Midnight today
            val yesterdayStart = todayStart.minusDays(1) // Midnight yesterday

            return when {
                countryTime.toLocalDate().isEqual(now.toLocalDate()) -> "Today"  // If it's today
                countryTime.toLocalDate()
                    .isEqual(yesterdayStart.toLocalDate()) -> "Yesterday"  // If it's yesterday
                else -> {
                    // Step 4: Format the date and time as you want
                    val outputFormatter =
                        DateTimeFormatter.ofPattern("HH:mm dd/MM", Locale.getDefault())
                    countryTime.format(outputFormatter)  // Format the time for other dates
                }
            }
        }
    }
}