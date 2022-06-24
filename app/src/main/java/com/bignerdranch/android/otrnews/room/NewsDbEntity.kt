package com.bignerdranch.android.otrnews.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bignerdranch.android.otrnews.room.dto.News

@Entity (tableName = "newsTable")

data class NewsDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val idSource: String,
    val title: String,
    val image: String,
    val localImage: String,
    val newsDate: String,
    val annotation: String,
    val idResource: String,
    val type: String,
    val newsDateUts: String,
    val mobileUrl: String
    ) {
    companion object {
        fun fromNews(news: News): NewsDbEntity = NewsDbEntity(
            id = 0,
            idSource = news.idSource,
            title = news.title,
            image = news.image,
            localImage = news.localImage,
            newsDate = news.newsDate,
            annotation = news.annotation,
            idResource = news.idResource,
            type = news.type,
            newsDateUts = news.newsDateUts,
            mobileUrl = news.mobileUrl
        )
    }
}