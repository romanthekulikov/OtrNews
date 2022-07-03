package com.bignerdranch.android.otrnews

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.otrnews.room.dto.News
import com.bignerdranch.android.otrnews.room.dto.ResponseData
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScrollingActivity : AppCompatActivity() {
    private lateinit var buttonAPI: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        buttonAPI = findViewById(R.id.getButton)

        buttonAPI.setOnClickListener {
            loadNews()
        }
    }

    private fun loadNews() {
        val service = RetrofitClientInstance.retrofitInstance?.create(GetNewsService::class.java)
        val call = service?.getAllNews()
        call?.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                buttonAPI.visibility = View.INVISIBLE
                val body = response.body()
                val news = body?.data?.news
                onNewsReceived(news)
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                getNewsFromFile()
                Toast.makeText(applicationContext, "Error reading JSON", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getNewsFromFile() {
        val json = assets.open("list.json").bufferedReader().use { it.readText() }
        val responseData = Gson().fromJson(json, ResponseData::class.java)
        onNewsReceived(responseData.data.news)
    }

    fun onNewsReceived(news: List<News>?) {
        importNewsToDataBase(news)
        displayNews(news)
    }

    fun importNewsToDataBase(news: List<News>?) {
        val size = news?.size
    }

    fun displayNews(news: List<News>?) {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}