package com.omarahmed.getnews2.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.omarahmed.getnews2.R
import com.omarahmed.getnews2.databinding.ItemLatestNewsBinding
import com.omarahmed.getnews2.data.room.NewsEntity
import com.omarahmed.getnews2.ui.home.NewsAdapter.LatestNewsViewHolder
import com.omarahmed.getnews2.util.DiffCallbackNewsEntity
import com.omarahmed.getnews2.util.setTimeAgo

class NewsAdapter(
    private val onBookmarkClick: (NewsEntity) -> Unit,
    private val onShareClick: (NewsEntity) -> Unit,
    private val onItemClick: (NewsEntity) -> Unit
) : ListAdapter<NewsEntity, LatestNewsViewHolder>(DiffCallbackNewsEntity()) {

    class LatestNewsViewHolder(
        private val binding: ItemLatestNewsBinding,
        private val onBookmarkClick: (Int) -> Unit,
        private val onShareClick: (Int) -> Unit,
        private val onItemClick: (Int) -> Unit
        ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION){
                        onItemClick(adapterPosition)
                    }
                }
                ivLatestNewsSave.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION){
                        onBookmarkClick(adapterPosition)
                    }
                }
                ivLatestNewsShare.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION){
                        onShareClick(adapterPosition)
                    }
                }
            }
        }

        fun bind(news: NewsEntity) {
            binding.apply {
                ivLatestNews.load(news.imageUrl){error(R.drawable.ic_error_placeholder)}
                tvLatestNewsTitle.text = news.title
                tvLatestNewsTime.setTimeAgo(news.publishedAt)
                ivLatestNewsSave.setImageResource(
                    when{
                        news.isBookmarked -> R.drawable.ic_bookmarked
                        else -> R.drawable.ic_bookmark_border
                    }
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestNewsViewHolder {
        val view = ItemLatestNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LatestNewsViewHolder(view,
            onItemClick = { position ->
                val news = getItem(position)
                if (news != null){
                    onItemClick(news)
                }
            },
            onBookmarkClick = { position ->
                val news = getItem(position)
                if (news != null){
                    onBookmarkClick(news)
                }
            },
            onShareClick = {position ->
                val news = getItem(position)
                if (news != null){
                    onShareClick(news)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: LatestNewsViewHolder, position: Int) {
        val currentNews = getItem(position)
        holder.bind(currentNews)
    }
}