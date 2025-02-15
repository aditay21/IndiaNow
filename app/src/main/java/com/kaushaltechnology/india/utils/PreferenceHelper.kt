package com.kaushaltechnology.india.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceHelper @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

    // Save preferences without passing context every time
    fun savePreference(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun savePreference(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getBooleanPreference(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun getStringPreference(key: String, defaultValue: String = "India"): String {
        return sharedPreferences.getString(key, defaultValue)?: defaultValue
    }
}
