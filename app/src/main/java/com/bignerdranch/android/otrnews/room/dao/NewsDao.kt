package com.bignerdranch.android.otrnews.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bignerdranch.android.otrnews.room.NewsDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM newsTable WHERE id = :newsId")
    fun getNewsById(newsId: Long) : Flow<NewsDbEntity?>
    
    @Insert
    suspend fun createNews(newsDbEntity: NewsDbEntity)
}