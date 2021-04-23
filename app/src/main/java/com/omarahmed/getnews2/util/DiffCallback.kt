package com.omarahmed.getnews2.util

import androidx.recyclerview.widget.DiffUtil
import com.omarahmed.getnews2.data.api.Article
import com.omarahmed.getnews2.data.room.NewsEntity

class DiffCallbackNewsEntity : DiffUtil.ItemCallback<NewsEntity>() {
    override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity) =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity) =
        oldItem == newItem
}

class DiffCallbackArticle: DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article) =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: Article, newItem: Article) =
        oldItem == newItem
}