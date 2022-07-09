package com.bignerdranch.android.otrnews.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.otrnews.entities.News


@Database(
    version = 1,
    entities = [ News::class ]
)
abstract class NewsDataBase : RoomDatabase() {
    abstract fun getNewsDao() : NewsDao
}