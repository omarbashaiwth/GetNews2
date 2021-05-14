package com.omarahmed.getnews2.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.omarahmed.getnews2.R
import com.omarahmed.getnews2.data.room.NewsEntity
import com.omarahmed.getnews2.databinding.ItemViewPagerBinding
import com.omarahmed.getnews2.util.DiffCallbackNewsEntity
import com.omarahmed.getnews2.util.setTimeAgo

class ViewPagerAdapter(
    private val onItemClick: (NewsEntity) -> Unit
): ListAdapter<NewsEntity, ViewPagerAdapter.ViewPagerViewHolder>(DiffCallbackNewsEntity()) {

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
        fun bind(article: NewsEntity){
            binding.apply {
                ivForYouNews.load(article.imageUrl){error(R.drawable.ic_error_placeholder)}
                tvForYouTitle.text = article.title
                tvSource.text = article.source
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