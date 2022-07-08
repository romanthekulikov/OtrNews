package com.bignerdranch.android.otrnews.data.dto.repository

import com.bignerdranch.android.otrnews.data.dto.News
import com.bignerdranch.android.otrnews.room.dao.NewsDao
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource
    private val localDataSource: NewsDao
){

}