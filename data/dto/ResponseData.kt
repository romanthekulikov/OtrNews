package com.bignerdranch.android.otrnews.data.dto

import com.google.gson.annotations.SerializedName

data class ResponseData(
    @SerializedName("data")
    val data: NewsList
)