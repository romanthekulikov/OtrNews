package com.bignerdranch.android.otrnews.retrofit

import com.bignerdranch.android.otrnews.entities.ResponseData
import retrofit2.Call
import retrofit2.http.GET

private const val ADDITIONAL_URL = "./api/mobile/news/list"

interface GetNewsService {
    @GET (ADDITIONAL_URL)
    fun getAllNews() : Call<ResponseData>
}