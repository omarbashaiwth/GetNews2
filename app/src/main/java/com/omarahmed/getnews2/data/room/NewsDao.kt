package com.omarahmed.getnews2.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLatestNews(latestNews: List<NewsEntity>)

    @Query("SELECT * FROM news_table WHERE newsType = 'LatestNews'")
    fun getLatestNews(): Flow<List<NewsEntity>>

    @Query("DELETE FROM news_table WHERE newsType ='LatestNews'")
    suspend fun deleteAllLatestNews()

    @Query("SELECT * FROM news_table WHERE isBookmarked = 1")
    fun getBookmarkedNews(): Flow<List<NewsEntity>>

    @Update
    suspend fun updateNews(latestNewsEntity: NewsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForYouNews(forYouNews: List<NewsEntity>)

    @Query("SELECT * FROM news_table WHERE newsType ='ForYouNews'")
    fun getForYouNews(): Flow<List<NewsEntity>>

    @Query("DELETE FROM news_table WHERE newsType ='ForYouNews'")
    suspend fun deleteAllForYouNews()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExploreNews(exploreNews: List<NewsEntity>)

    @Query("SELECT * FROM news_table WHERE newsType = 'ExploreNews'")
    fun getExploreNews(): Flow<List<NewsEntity>>

    @Query("DELETE FROM news_table WHERE newsType ='ExploreNews' AND isBookmarked = 0")
    suspend fun clearExploreNews()
}