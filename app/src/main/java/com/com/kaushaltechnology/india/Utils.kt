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


    }
}