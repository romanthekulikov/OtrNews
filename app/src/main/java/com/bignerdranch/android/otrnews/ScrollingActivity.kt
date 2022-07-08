package com.bignerdranch.android.otrnews

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.otrnews.databinding.ActivityScrollingBinding
import com.bignerdranch.android.otrnews.room.dto.News
import com.bignerdranch.android.otrnews.room.dto.ResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private const val INTENT_NEWS_KEY = "Key1"

class ScrollingActivity : AppCompatActivity(), AdapterNews.NewsListener {

    private lateinit var binding: ActivityScrollingBinding
    private lateinit var adapter: AdapterNews


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        loadData()
        
        binding.updateNews.setOnClickListener{
            loadData()
        }
    }

    private fun loadData() {
        val service = RetrofitClientInstance.retrofitInstance?.create(GetNewsService::class.java)
        val call = service?.getAllNews()
        call?.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                binding.progress.isVisible = false

                val body = response.body()
                val news = body?.data?.news ?: emptyList()
                adapter.updateAdapter(news)
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                binding.progress.isVisible = false
                Toast.makeText(applicationContext, "Error reading JSON", Toast.LENGTH_LONG).show()
            }
        })
    }
    
    private fun initAdapter() {
        adapter = AdapterNews(ArrayList(), this)

        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onClickNews(news: News) {
        startActivity(Intent(this, NewsActivity::class.java).apply {
            putExtra(INTENT_NEWS_KEY, news)
        })
    }
}