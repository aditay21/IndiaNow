package com.kaushaltechnology.india.utils

import android.content.Context
import android.content.Intent


class Utils {

    companion object {

        val TAG = Companion::class.java.simpleName

        // Static-like method
        fun replaceSpecialChar(text: String): String {
            return text.replace("&#039;", "")
        }
        private fun shareApp(newsUrl: String, context: Context) {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"  // Move this line outside the block properly
                putExtra(Intent.EXTRA_SUBJECT, "Check out this amazing app!")

                val shareText = if (newsUrl.isNotEmpty()) {
                    "Check out this news: $newsUrl\n\nAlso, For authentic news, please please check out this: https://play.google.com/store/apps/details?id=com.aditechnology.tambola"
                } else {
                    "Hey, For authentic news, please check out this app: https://play.google.com/store/apps/details?id=com.aditechnology.tambola"
                }

                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        fun shareTheAPPAndNews(newsUrl: String, context: Context) {
            shareApp(newsUrl, context)
        }


    }
}