package com.bignerdranch.android.otrnews.di

import android.content.Context
import com.bignerdranch.android.otrnews.GetNewsService
import com.bignerdranch.android.otrnews.data.dto.repository.NewsRepository
import com.bignerdranch.android.otrnews.room.dao.NewsDao
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private var BASE_URL = "https://ws-tszh-1c-test.vdgb-soft.ru/"
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideCharacterService(retrofit: Retrofit): GetNewsService = retrofit.create(GetNewsService::class.java)

    @Singleton
    @Provides
    fun provideCharacterRemoteDataSource(characterService: GetNewsService) = NewsRemoteDataSource(characterService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideCharacterDao(db: AppDatabase) = db.characterDao()

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: NewsRemoteDataSource,
                          localDataSource: NewsDao) =
        NewsRepository(remoteDataSource, localDataSource)
}