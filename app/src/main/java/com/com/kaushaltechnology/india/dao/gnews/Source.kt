package com.com.kaushaltechnology.india.dao.gnews

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id") @ColumnInfo(name = "source_id") val id: String?,
    @SerializedName("name") @ColumnInfo(name = "source_name") val name: String)