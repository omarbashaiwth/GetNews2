package com.omarahmed.getnews2.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLatestNews(latestNews: List<LatestNewsEntity>)

    @Query("SELECT * FROM latest_news_table")
    fun getLatestNews(): Flow<List<LatestNewsEntity>>

    @Query("DELETE FROM latest_news_table")
    suspend fun deleteAllLatestNews()

    @Query("SELECT * FROM latest_news_table WHERE isBookmarked = 1")
    fun getBookmarkedNews(): Flow<List<LatestNewsEntity>>

    @Update
    suspend fun updateNews(latestNewsEntity: LatestNewsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForYouNews(forYouNews: List<ForYouNewsEntity>)

    @Query("SELECT * FROM for_you_news_table")
    fun getForYouNews(): Flow<List<ForYouNewsEntity>>

    @Query("DELETE FROM for_you_news_table")
    suspend fun deleteAllForYouNews()
}