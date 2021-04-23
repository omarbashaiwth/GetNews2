package com.omarahmed.getnews2.data.room

import androidx.room.*
import com.omarahmed.getnews2.data.api.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLatestNews(news: List<NewsEntity>)

    @Query("SELECT * FROM news_table")
    fun getLatestNews(): Flow<List<NewsEntity>>

    @Query("DELETE FROM news_table")
    suspend fun deleteAllLatestNews()

    @Query("SELECT * FROM news_table WHERE isBookmarked = 1")
    fun getBookmarkedNews(): Flow<List<NewsEntity>>

    @Update
    suspend fun updateNews(newsEntity: NewsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForYouNews(forYouNews: List<Article>)

    @Query("SELECT * FROM for_you_news")
    fun getForYouNews(): Flow<List<Article>>

    @Query("DELETE FROM for_you_news")
    suspend fun deleteAllForYouNews()
}