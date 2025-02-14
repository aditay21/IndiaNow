package com.kaushaltechnology.india


class Utils {

    companion object {

        val TAG = Companion::class.java.simpleName

        // Static-like method
        fun replaceSpecialChar(text: String): String {
            return text.replace("&#039;", "")
        }
    }
}