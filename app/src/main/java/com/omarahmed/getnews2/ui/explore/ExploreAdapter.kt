package com.omarahmed.getnews2.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.omarahmed.getnews2.R
import com.omarahmed.getnews2.data.room.NewsEntity
import com.omarahmed.getnews2.databinding.ItemExploreNewsBinding
import com.omarahmed.getnews2.util.DiffCallbackNewsEntity
import com.omarahmed.getnews2.util.setTimeAgo

class ExploreAdapter(
    private val onShareClick: (NewsEntity) -> Unit,
    private val onBookmarked: (NewsEntity) -> Unit
) : ListAdapter<NewsEntity, ExploreAdapter.ExploreViewHolder>(DiffCallbackNewsEntity()){

    class ExploreViewHolder(
        private val binding: ItemExploreNewsBinding,
        onShareClick: (Int) -> Unit,
        onBookmarked: (Int) -> Unit
        ): RecyclerView.ViewHolder(binding.root){

        init {
            binding.ivExploreShare.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION){
                    onShareClick(adapterPosition)
                }
            }
            binding.ivExploreSave.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION){
                    onBookmarked(adapterPosition)
                }
            }
        }
        fun bind(article: NewsEntity){
            binding.apply {
                ivExploreNews.load(article.imageUrl){
                    error(R.drawable.ic_error_placeholder)
                }
                tvExploreTitle.text = article.title
                tvExploreDesc.text = article.desc
                tvExploreTime.setTimeAgo(article.publishedAt)
                ivExploreSave.setImageResource(
                    when{
                        article.isBookmarked -> R.drawable.ic_bookmarked
                        else -> R.drawable.ic_bookmark_border
                    }
                )
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
            },
            onBookmarked = {position ->
                val news = getItem(position)
                if (news != null){
                    onBookmarked(news)
                }
            }
        )
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
