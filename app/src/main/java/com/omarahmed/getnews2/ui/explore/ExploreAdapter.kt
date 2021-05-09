package com.omarahmed.getnews2.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.omarahmed.getnews2.R
import com.omarahmed.getnews2.data.api.Article
import com.omarahmed.getnews2.databinding.ItemExploreNewsBinding
import com.omarahmed.getnews2.util.DiffCallbackArticle
import com.omarahmed.getnews2.util.setTimeAgo

class ExploreAdapter(
 private val onShareClick: (Article) -> Unit
) : ListAdapter<Article, ExploreAdapter.ExploreViewHolder>(DiffCallbackArticle()){

    class ExploreViewHolder(
        private val binding: ItemExploreNewsBinding,
        onShareClick: (Int) -> Unit
        ): RecyclerView.ViewHolder(binding.root){

        init {
            binding.ivExploreShare.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION){
                    onShareClick(adapterPosition)
                }
            }
        }
        fun bind(article: Article){
            binding.apply {
                ivExploreNews.load(article.urlToImage){
                    error(R.drawable.ic_error_placeholder)
                }
                tvExploreTitle.text = article.title
                tvExploreDesc.text = article.description
                tvExploreTime.setTimeAgo(article.publishedAt)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val view = ItemExploreNewsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ExploreViewHolder(view,
            onShareClick = { position ->
                val news = getItem(position)
                if (news != null){
                    onShareClick(news)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
