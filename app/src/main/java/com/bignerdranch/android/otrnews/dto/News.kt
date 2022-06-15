package com.bignerdranch.android.otrnews.dto

data class News (
    var id: Int,
    var title: String,
    var img: String,
    var newsDate: String,
    var annotation: String,
    var type: Int,
    var newsDateUts: String,
    var mobileUrl: String
)