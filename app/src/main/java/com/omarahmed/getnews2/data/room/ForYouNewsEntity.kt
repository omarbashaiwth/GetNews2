package com.omarahmed.getnews2.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "for_you_news_table")
data class ForYouNewsEntity(
    val title: String,
    @PrimaryKey val url: String,
    val imageUrl: String?,
    val publishedAt: String?,
    val source: String?,
    val desc: String?,
    val updatedAt: Long = System.currentTimeMillis()
)
