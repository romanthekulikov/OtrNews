package com.bignerdranch.android.otrnews.news

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.otrnews.data.dto.repository.NewsRepository
import com.bumptech.glide.load.engine.Resource


class NewsViewModel @ViewModelInject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    val characters: LiveData<Resource<List<Character>>> = repository.getCharacters()
}