package com.omarahmed.getnews2.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "latest_news_table")
data class LatestNewsEntity(
    val title: String,
    @PrimaryKey val url: String,
    val imageUrl: String?,
    val publishedAt: String?,
    val source: String?,
    val desc: String?,
    val isBookmarked: Boolean,
    val updatedAt: Long = System.currentTimeMillis()
)