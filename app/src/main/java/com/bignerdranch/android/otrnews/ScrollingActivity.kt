package com.bignerdranch.android.otrnews

 import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
    
        initAdapter()
        newsDataBase = Room.databaseBuilder(
            this,
            NewsDataBase::class.java,
            "newsTable"
        ).fallbackToDestructiveMigration().build()
        
        loadFromDB()
        
        binding.updateNews.setOnClickListener {
            loadData()
        }
    
        searchNews()
        
        val uninterestingNewsLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_CANCELED) {
                loadFromDB()
            }
        }
        
        binding.nonInterestingNews.setOnClickListener {
            val intent = UninterestingNewsActivity.newIntent(this)
            uninterestingNewsLauncher.launch(intent)
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
                insertNewsToDataBase(news)
                loadFromDB()
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                binding.progress.isVisible = false
                binding.updateNews.isEnabled = true
                
                Toast.makeText(applicationContext, "Error reading JSON", Toast.LENGTH_LONG).show()
            }
        })
    }
    
    private fun searchNews() {
        binding.searchMagnifier.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(searchString: String): Boolean {
                loadNewsBySearchString(searchString)
                binding.updateNews.isVisible = true
                binding.nonInterestingNews.isVisible = true
                return false
            }

            override fun onQueryTextChange(searchString: String): Boolean {
                loadNewsBySearchString(searchString)
                return false
            }
        })
    }
    
    private fun loadFromDB() {
        binding.progress.isVisible = true
        
        val newsDao = newsDataBase.NewsDao()
        
        lifecycleScope.launch(Dispatchers.IO) {
            val news = newsDao.getNoHiddenNews()
            withContext(Dispatchers.Main) {
                binding.progress.isVisible = false
                adapter.updateAdapter(news)
            }
        }
    }
    
    private fun loadNewsBySearchString(searchString: String) {
        binding.progress.isVisible = true
        val newsDao = newsDataBase.NewsDao()
        lifecycleScope.launch(Dispatchers.IO) {
            val news = newsDao.getSearchNews(searchString)
        
            withContext(Dispatchers.Main) {
                binding.progress.isVisible = false
                adapter.updateAdapter(news)
            }
        }
    }
    
    private fun insertNewsToDataBase(news: List<News>) {
        val newsDao = newsDataBase.NewsDao()
        lifecycleScope.launch(Dispatchers.IO) {
            val hiddenNews = newsDao.getHiddenNews()
            newsDao.insertNews(news)
            newsDao.insertNews(hiddenNews)
        }
    }
    
    private fun hideNews(newsItem: News) {
        val idNews = newsItem.id
        val newsDao = newsDataBase.NewsDao()
        lifecycleScope.launch(Dispatchers.IO) {
            newsDao.updateNewsHidingById(idNews, "false")
            val news = newsDao.getNoHiddenNews()
            withContext(Dispatchers.Main) {
                adapter.updateAdapter(news)
            }
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
    override fun buildInterestDialog(news: News) {
        val interestDialogBuilder = AlertDialog.Builder(this)
        interestDialogBuilder.setMessage("Поместить новость в \"Не интересно\"?")
        interestDialogBuilder.setNegativeButton("Нет") { dialog, _ ->
            dialog.cancel()
        }
        interestDialogBuilder.setPositiveButton("Да") { dialog, _ ->
            hideNews(news)
            Toast.makeText(
                this,
                "Новость перемещена в раздел \"Не интересно\"",
                Toast.LENGTH_SHORT
            ).show()
            dialog.cancel()
        }
        interestDialogBuilder.show()
    }
}