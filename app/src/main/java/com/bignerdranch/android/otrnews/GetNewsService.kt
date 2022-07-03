package com.bignerdranch.android.otrnews

import com.bignerdranch.android.otrnews.room.dto.ResponseData
import retrofit2.Call
import retrofit2.http.GET

private const val ADDITIONAL_URL = "./api/mobile/news/list"

interface GetNewsService {
    @GET (ADDITIONAL_URL)
    fun getAllNews() : Call<ResponseData>
}