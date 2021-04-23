package com.omarahmed.getnews2.data.api

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "for_you_news")
data class Article(
    val title: String,
    @PrimaryKey val url: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val source: Source
)

data class Source(
    val name: String?
)