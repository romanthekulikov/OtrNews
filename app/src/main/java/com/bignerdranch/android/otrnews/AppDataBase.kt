package com.bignerdranch.android.otrnews

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.otrnews.room.NewsDbEntity
import com.bignerdranch.android.otrnews.room.dao.NewsDao


@Database(
    version = 1,
    entities = [ NewsDbEntity::class ]
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getNewsDao() : NewsDao
}