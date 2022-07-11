package com.bignerdranch.android.otrnews

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bignerdranch.android.otrnews.adapter.AdapterNews
import com.bignerdranch.android.otrnews.dataBase.NewsDataBase
import com.bignerdranch.android.otrnews.databinding.ActivityScrollingBinding
import com.bignerdranch.android.otrnews.entities.News
import com.bignerdranch.android.otrnews.entities.ResponseData
import com.bignerdranch.android.otrnews.retrofit.GetNewsService
import com.bignerdranch.android.otrnews.retrofit.RetrofitClientInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private const val INTENT_NEWS_KEY = "Key1"

class ScrollingActivity : AppCompatActivity(), AdapterNews.NewsListener {

    private lateinit var binding: ActivityScrollingBinding
    private lateinit var adapter: AdapterNews
    private lateinit var newsDataBase: NewsDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    
        newsDataBase =  Room.databaseBuilder(
            this,
            NewsDataBase::class.java,
            "newsTable"
        ).build()
        
        initAdapter()
        loadFromDB()
        
        binding.updateNews.setOnClickListener {
            loadData()
        }
    }
    
    private fun loadFromDB() {
        binding.progress.isVisible = true
        
        val newsDao = newsDataBase.getNewsDao()
    
        lifecycleScope.launch(Dispatchers.IO) {
            val news = newsDao.getNews()
            
            withContext(Dispatchers.Main) {
                binding.progress.isVisible = false
                adapter.updateAdapter(news)
            }
        }
    }

    private fun loadData() {
        binding.progress.isVisible = true
        binding.updateNews.isEnabled = false
        
        val service = RetrofitClientInstance.retrofitInstance?.create(GetNewsService::class.java)
        val call = service?.getAllNews()
        call?.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                binding.progress.isVisible = false
                binding.updateNews.isEnabled = true
                
                val body = response.body()
                val news = body?.data?.news ?: emptyList()
                adapter.updateAdapter(news)
    
                insertNewsToDataBase(news)
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                binding.progress.isVisible = false
                binding.updateNews.isEnabled = true
                
                Toast.makeText(applicationContext, "Error reading JSON", Toast.LENGTH_LONG).show()
            }
        })
    }
    
    private fun insertNewsToDataBase(news: List<News>) {
        val newsDao = newsDataBase.getNewsDao()
    
        lifecycleScope.launch(Dispatchers.IO) {
            newsDao.insertNews(news)
        }
    }
    
    private fun initAdapter() {
        adapter = AdapterNews(ArrayList(), this)

        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter
    }
    
    override fun onClickNews(news: News) {
        startActivity(Intent(this, NewsActivity::class.java).apply {
            putExtra(INTENT_NEWS_KEY, news)
        })
    }
}