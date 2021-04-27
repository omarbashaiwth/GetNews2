package com.omarahmed.getnews2.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.omarahmed.getnews2.data.api.Article
import com.omarahmed.getnews2.databinding.ItemViewPagerBinding
import com.omarahmed.getnews2.util.setTimeAgo

class ViewPagerAdapter(
    private val onItemClick: (Article) -> Unit
): ListAdapter<Article, ViewPagerAdapter.ViewPagerViewHolder>(DiffCallback()) {

    class DiffCallback: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) =
            oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Article, newItem: Article) =
            oldItem == newItem
    }

    class ViewPagerViewHolder(
        private val binding: ItemViewPagerBinding,
        private val onItemClick: (Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION){
                    onItemClick(adapterPosition)
                }
            }
        }
        fun bind(article: Article){
            binding.apply {
                ivForYouNews.load(article.urlToImage)
                tvForYouTitle.text = article.title
                tvSource.text = article.source.name
                tvForYouDate.setTimeAgo(article.publishedAt)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = ItemViewPagerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewPagerViewHolder(
            view,
            onItemClick = {position ->
                val news = getItem(position)
                if (news != null){
                    onItemClick(news)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}