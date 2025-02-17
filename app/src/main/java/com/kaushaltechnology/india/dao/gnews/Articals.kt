package com.kaushaltechnology.india.dao.gnews

import androidx.room.Entity

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Entity(tableName = "news")
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,  // Add primary key with auto-generate
    @SerializedName("title") @ColumnInfo(name = "title") val title: String,
    @SerializedName("description") @ColumnInfo(name = "description") val description: String,
    @SerializedName("url") @ColumnInfo(name = "url") val url: String,
    @SerializedName("image") @ColumnInfo(name = "url_to_image") val urlToImage: String?,
    @SerializedName("publishedAt") @ColumnInfo(name = "published_at") val publishedAt: String,
    @Embedded val source: Source,
    @ColumnInfo(name = "read") val read: Boolean = false,
    @ColumnInfo(name = "category") val category: String = "General"
){
    fun getFormattedPublishedAt(): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Set input format timezone
            val date = inputFormat.parse(publishedAt)

            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            outputFormat.format(date ?: Date()) // Return formatted date
        } catch (e: Exception) {
            publishedAt // Return original if parsing fails
        }
    }
}


