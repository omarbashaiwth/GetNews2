package com.omarahmed.getnews2.ui.bookmarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.omarahmed.getnews2.R
import com.omarahmed.getnews2.data.room.LatestNewsEntity
import com.omarahmed.getnews2.databinding.ItemBookmarksBinding
import com.omarahmed.getnews2.util.DiffCallbackNewsEntity
import com.omarahmed.getnews2.util.setTimeAgo

class BookmarksAdapter(
    private val onBookmarkClick: (LatestNewsEntity) -> Unit,
    private val onShareClick: (LatestNewsEntity) -> Unit,
    private val onItemClick: (LatestNewsEntity) -> Unit
) : ListAdapter<LatestNewsEntity, BookmarksAdapter.BookmarksViewHolder>(DiffCallbackNewsEntity()) {

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

        fun bind(latestNewsEntity: LatestNewsEntity) {
            binding.apply {
                ivSavedNews.load(latestNewsEntity.imageUrl){error(R.drawable.ic_error_placeholder)}
                tvSavedNewsTitle.text = latestNewsEntity.title
                tvSavedNewsDesc.text = latestNewsEntity.desc
                tvSavedNewsTime.setTimeAgo(latestNewsEntity.publishedAt)
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