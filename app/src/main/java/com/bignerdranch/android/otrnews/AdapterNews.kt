package com.bignerdranch.android.otrnews

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.otrnews.databinding.RcNewsitemBinding
import com.bignerdranch.android.otrnews.room.dto.News
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class AdapterNews(
    private var listItems: ArrayList<News>,
    private val listener: NewsListener
) : RecyclerView.Adapter<AdapterNews.NewsItemHolder>() {

    private lateinit var el: RcNewsitemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        el = RcNewsitemBinding.inflate(inflater, parent,false)
        return NewsItemHolder(el.root, el)
    }

    override fun onBindViewHolder(holder: NewsItemHolder, position: Int) {
        holder.drawItem(listItems[position], listener)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(items: List<News>){
        listItems.clear()
        listItems.addAll(items)
        notifyDataSetChanged()
    }

    class NewsItemHolder(
        itemView: View,
        private val el: RcNewsitemBinding,
    ) : RecyclerView.ViewHolder(itemView) {

        fun drawItem(item: News, listener: NewsListener) {
            el.title.text = item.title
            Glide.with(el.root)
                .load(item.image)
                .into(el.image)
            itemView.setOnClickListener{
                listener.onClickNews(item)
            }
        }
    }
    
    interface NewsListener{
        fun onClickNews(news: News)
    }
}