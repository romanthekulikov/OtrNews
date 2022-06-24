package com.bignerdranch.android.otrnews.room.dto

import com.google.gson.annotations.SerializedName

data class News (
    @SerializedName("id")
    val idSource: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("img")
    val image: String,
    @SerializedName("local_img")
    val localImage: String,
    @SerializedName("news_date")
    val newsDate: String,
    @SerializedName("annotation")
    val annotation: String,
    @SerializedName("id_resource")
    val idResource: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("news_date_uts")
    val newsDateUts: String,
    @SerializedName("mobile_url")
    val mobileUrl: String
)