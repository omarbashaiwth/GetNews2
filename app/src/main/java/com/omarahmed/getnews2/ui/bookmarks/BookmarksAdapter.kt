package com.omarahmed.getnews2.ui.bookmarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.omarahmed.getnews2.R
import com.omarahmed.getnews2.data.room.NewsEntity
import com.omarahmed.getnews2.databinding.ItemBookmarksBinding
import com.omarahmed.getnews2.util.DiffCallbackNewsEntity
import com.omarahmed.getnews2.util.setTimeAgo

class BookmarksAdapter(
    private val onBookmarkClick: (NewsEntity) -> Unit,
    private val onShareClick: (NewsEntity) -> Unit,
    private val onItemClick: (NewsEntity) -> Unit
) : ListAdapter<NewsEntity, BookmarksAdapter.BookmarksViewHolder>(DiffCallbackNewsEntity()) {

    class BookmarksViewHolder(
        private val binding: ItemBookmarksBinding,
        private val onBookmarkClick: (Int) -> Unit,
        private val onShareClick: (Int) -> Unit,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onItemClick(adapterPosition)
                    }
                }
                ivUnBookmark.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onBookmarkClick(adapterPosition)
                    }
                }
                ivSavedNewsShare.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onShareClick(adapterPosition)
                    }
                }
            }
        }

        fun bind(newsEntity: NewsEntity) {
            binding.apply {
                ivSavedNews.load(newsEntity.imageUrl){error(R.drawable.ic_error_placeholder)}
                tvSavedNewsTitle.text = newsEntity.title
                tvSavedNewsDesc.text = newsEntity.desc
                tvSavedNewsTime.setTimeAgo(newsEntity.publishedAt)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksViewHolder {
        val view =
            ItemBookmarksBinding.inflate((LayoutInflater.from(parent.context)), parent, false)
        return BookmarksViewHolder(
            view,
            onItemClick = { position ->
                val news = getItem(position)
                onItemClick(news)
            },
            onBookmarkClick = { position ->
                val news = getItem(position)
                onBookmarkClick(news)
            },
            onShareClick = {position ->
                val news = getItem(position)
                onShareClick(news)
            }
        )
    }

    override fun onBindViewHolder(holder: BookmarksViewHolder, position: Int) {
        val currentNews = getItem(position)
        holder.bind(currentNews)
    }
}