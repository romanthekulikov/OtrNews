package com.bignerdranch.android.otrnews

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bignerdranch.android.otrnews.databinding.ActivityNewsBinding
import com.bignerdranch.android.otrnews.entities.News
import com.bumptech.glide.Glide

private const val INTENT_NEWS_KEY = "Key1"

class NewsActivity : AppCompatActivity() {
	private lateinit var binding: ActivityNewsBinding
	
	@SuppressLint("CheckResult")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityNewsBinding.inflate(layoutInflater)
		setContentView(binding.root)
		
		
		val item = intent.getParcelableExtra<News>(INTENT_NEWS_KEY)
		if (item != null) {
			binding.apply {
				titleNews.text = item.title
				annotationNews.text = item.annotation
				urlNews.text = item.mobileUrl
				Glide.with(this.root)
					.load(item.image)
					.into(newsImage)
			}
		} else {
			Toast.makeText(
				this,
				"Этой новости нет",
				Toast.LENGTH_SHORT
			).show()
		}
	}
}