package com.bignerdranch.android.otrnews

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bignerdranch.android.otrnews.adapter.AdapterNews
import com.bignerdranch.android.otrnews.dataBase.NewsDataBase
import com.bignerdranch.android.otrnews.databinding.ActivityUninterestingNewsBinding
import com.bignerdranch.android.otrnews.entities.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val INTENT_NEWS_KEY = "Key1"

class UninterestingNewsActivity : AppCompatActivity(), AdapterNews.NewsListener {
	private lateinit var binding: ActivityUninterestingNewsBinding
	private lateinit var newsDataBase: NewsDataBase
	private lateinit var adapter: AdapterNews
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityUninterestingNewsBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		newsDataBase = Room.databaseBuilder(
			this,
			NewsDataBase::class.java,
			"newsTable"
		).build()
		initAdapter()
		loadHiddenNewsFromDB()
		
		searchNews()
		
		binding.interestingNews.setOnClickListener {
			finish()
		}
	}
	
	private fun loadHiddenNewsFromDB() {
		binding.progress.isVisible = true
		val newsDao = newsDataBase.NewsDao()
		
		lifecycleScope.launch(Dispatchers.IO) {
			val news = newsDao.getHiddenNews()
			
			withContext(Dispatchers.Main) {
				binding.progress.isVisible = false
				adapter.updateAdapter(news)
			}
		}
	}
	
	private fun initAdapter() {
		adapter = AdapterNews(ArrayList(), this)
		
		binding.rcView.layoutManager = LinearLayoutManager(this)
		binding.rcView.adapter = adapter
	}
	
	private fun unHideNews(newsItem: News) {
		val idNews = newsItem.id
		val newsDao = newsDataBase.NewsDao()
		lifecycleScope.launch(Dispatchers.IO) {
			newsDao.updateNewsHidingById(idNews, "true")
			val news = newsDao.getHiddenNews()
			withContext(Dispatchers.Main) {
				adapter.updateAdapter(news)
			}
		}
	}
	
	private fun searchNews() {
		binding.searchMagnifier.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
			override fun onQueryTextSubmit(searchString: String): Boolean {
				loadNewsBySearchString(searchString)
				return false
			}
			
			override fun onQueryTextChange(searchString: String): Boolean {
				loadNewsBySearchString(searchString)
				return false
			}
		})
	}
	
	private fun loadNewsBySearchString(searchString: String) {
		binding.progress.isVisible = true
		val newsDao = newsDataBase.NewsDao()
		lifecycleScope.launch(Dispatchers.IO) {
			val news = newsDao.getSearchHidingNews(searchString)
			
			withContext(Dispatchers.Main) {
				binding.progress.isVisible = false
				adapter.updateAdapter(news)
			}
		}
	}
	
	override fun onClickNews(news: News) {
		startActivity(Intent(this, NewsActivity::class.java).apply {
			putExtra(INTENT_NEWS_KEY, news)
		})
	}
	
	override fun buildInterestDialog(news: News) {
		val interestDialogBuilder = AlertDialog.Builder(this)
		interestDialogBuilder.setMessage("Вернуть новость в \"Интересно\"?")
		interestDialogBuilder.setNegativeButton("Нет") { dialog, _ ->
			dialog.cancel()
		}
		interestDialogBuilder.setPositiveButton("Да") { dialog, _ ->
			unHideNews(news)
			Toast.makeText(
				this,
				"Новость перемещена в раздел \"Интересно\"",
				Toast.LENGTH_SHORT
			).show()
			dialog.cancel()
		}
		interestDialogBuilder.show()
	}
}
