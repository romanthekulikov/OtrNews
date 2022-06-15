package com.bignerdranch.android.otrnews.dto

import com.google.gson.annotations.SerializedName

data class NewsList(@SerializedName("Block") var news: List<News>) {
}