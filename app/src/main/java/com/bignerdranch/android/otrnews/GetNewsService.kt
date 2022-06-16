package com.bignerdranch.android.otrnews

import com.bignerdranch.android.otrnews.dto.NewsList
import retrofit2.Call
import retrofit2.http.GET

public interface GetNewsService {
    @GET ("/api/mobile/news/list")
    fun getAllNews() : Call<NewsList>
}