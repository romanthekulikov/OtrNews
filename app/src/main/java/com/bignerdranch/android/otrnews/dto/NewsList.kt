package com.bignerdranch.android.otrnews.dto

import com.google.gson.annotations.SerializedName

data class NewsList(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("news")
    val news: List<News>,
    @SerializedName("count")
    val countNews: String,
    @SerializedName("error_msg")
    val errorMessage: String,
    @SerializedName("Here")
    val here: String

)