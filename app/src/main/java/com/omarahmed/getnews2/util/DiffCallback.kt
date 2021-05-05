package com.omarahmed.getnews2.util

import androidx.recyclerview.widget.DiffUtil
import com.omarahmed.getnews2.data.api.Article
import com.omarahmed.getnews2.data.room.ForYouNewsEntity
import com.omarahmed.getnews2.data.room.LatestNewsEntity

class DiffCallbackNewsEntity : DiffUtil.ItemCallback<LatestNewsEntity>() {
    override fun areItemsTheSame(oldItem: LatestNewsEntity, newItem: LatestNewsEntity) =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: LatestNewsEntity, newItem: LatestNewsEntity) =
        oldItem == newItem
}

class DiffCallbackArticle: DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article) =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: Article, newItem: Article) =
        oldItem == newItem
}

class DiffCallbackForYouNewsEntity : DiffUtil.ItemCallback<ForYouNewsEntity>() {
    override fun areItemsTheSame(oldItem: ForYouNewsEntity, newItem: ForYouNewsEntity) =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: ForYouNewsEntity, newItem: ForYouNewsEntity) =
        oldItem == newItem
}