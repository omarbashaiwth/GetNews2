package com.omarahmed.getnews2.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.omarahmed.getnews2.data.api.Article
import com.omarahmed.getnews2.databinding.ItemLatestNewsBinding
import com.omarahmed.getnews2.util.DiffCallbackArticle
import com.omarahmed.getnews2.util.setTimeAgo

class SearchAdapter(
    private val listener: OnItemClickListener
): ListAdapter<Article, SearchAdapter.SearchViewHolder>(DiffCallbackArticle()) {

    inner class SearchViewHolder(private val binding: ItemLatestNewsBinding, ): RecyclerView.ViewHolder(binding.root){

        init {
           binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION){
                    listener.onItemClick(getItem(adapterPosition))
                }
           }
        }
        fun bind(article: Article){
            binding.apply {
                ivLatestNews.load(article.urlToImage)
                tvLatestNewsTitle.text = article.title
                tvLatestNewsTime.setTimeAgo(article.publishedAt)
                ivLatestNewsShare.isVisible = false
                ivLatestNewsSave.isVisible = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = ItemLatestNewsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchViewHolder(
            view,
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnItemClickListener{
        fun onItemClick(article: Article)
    }
}