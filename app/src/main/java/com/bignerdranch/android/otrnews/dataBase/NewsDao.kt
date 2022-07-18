package com.bignerdranch.android.otrnews.dataBase

import androidx.room.*
import com.bignerdranch.android.otrnews.entities.News

@Dao
interface NewsDao {
    @Query("SELECT * FROM newsTable")
    fun getNews() : List<News>
    
    @Query("SELECT * FROM newsTable WHERE visibility = 'true'")
    fun getNoHiddenNews() : List<News>
    
    @Query("SELECT * FROM newsTable WHERE visibility = 'false'")
    fun getHiddenNews() : List<News>

    @Query("SELECT * FROM newsTable WHERE visibility = :hided")
    fun getNewsByHiding(hided: String) : List<News>
    
    @Query("SELECT * FROM newsTable WHERE title LIKE '%' || :searchString || '%' AND visibility = 'true'")
    fun getSearchNews(searchString: String) : List<News>
    
    @Query("SELECT * FROM newsTable WHERE title LIKE '%' || :searchString || '%' AND visibility = 'false'")
    fun getSearchHidingNews(searchString: String) : List<News>
    
    @Query("UPDATE newsTable SET visibility = :newsVisibility WHERE id = :newsId")
    fun updateNewsHidingById(newsId: String, newsVisibility: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news: List<News>)
}
