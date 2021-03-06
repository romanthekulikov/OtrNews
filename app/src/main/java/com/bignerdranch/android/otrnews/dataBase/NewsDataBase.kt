package com.bignerdranch.android.otrnews.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.otrnews.entities.News

@Database(
    version = 2,
    entities = [ News::class ]
)
abstract class NewsDataBase : RoomDatabase() {
    abstract fun NewsDao() : NewsDao
}

