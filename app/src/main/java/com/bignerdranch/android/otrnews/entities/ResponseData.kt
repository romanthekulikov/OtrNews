package com.bignerdranch.android.otrnews.entities

import com.google.gson.annotations.SerializedName

data class ResponseData(
    @SerializedName("data")
    val data: NewsList
)
