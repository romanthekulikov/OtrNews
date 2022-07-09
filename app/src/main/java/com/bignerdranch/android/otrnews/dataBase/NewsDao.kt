package com.bignerdranch.android.otrnews.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bignerdranch.android.otrnews.entities.News

@Dao
interface NewsDao {
    @Query("SELECT * FROM newsTable")
    fun getNews() : List<News>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news: List<News>)
}