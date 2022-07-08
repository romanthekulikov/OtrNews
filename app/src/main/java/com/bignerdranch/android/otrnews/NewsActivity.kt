package com.bignerdranch.android.otrnews

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.otrnews.databinding.ActivityNewsBinding
import com.bignerdranch.android.otrnews.room.dto.News
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
		binding.apply {
			titleNews.text = item?.title
			annotationNews.text = item?.annotation
			urlNews.text = item?.mobileUrl
			if (item != null) {
				Glide.with(this.root)
					.load(item.image)
					.into(newsImage)
			}
		}
	}
}