package com.omarahmed.getnews2.data

import androidx.room.withTransaction
import com.omarahmed.getnews2.data.api.NewsApi
import com.omarahmed.getnews2.data.room.ForYouNewsEntity
import com.omarahmed.getnews2.data.room.NewsDatabase
import com.omarahmed.getnews2.data.room.LatestNewsEntity
import com.omarahmed.getnews2.util.Resource
import com.omarahmed.getnews2.util.networkBoundResource
import com.omarahmed.getnews2.util.networkBoundResourceApiOnly
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: NewsApi,
    private val db: NewsDatabase
) {
    private val dao = db.newsDao()

    fun getLatestNews(
        forceRefresh: Boolean,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<LatestNewsEntity>>> = networkBoundResource(
        query = {dao.getLatestNews()},
        fetch = {api.getLatestNews().articles},
        saveFetchResult = { articles ->
            val bookmarkedNews = dao.getBookmarkedNews().first()
            val latestNews = articles.map { serverNews ->
                val isBookmarked = bookmarkedNews.any { latestNewsEntity ->
                    latestNewsEntity.url == serverNews.url
                }
                LatestNewsEntity(
                    title = serverNews.title,
                    url = serverNews.url,
                    imageUrl = serverNews.urlToImage,
                    publishedAt = serverNews.publishedAt,
                    source = serverNews.source.name,
                    desc = serverNews.description,
                    isBookmarked = isBookmarked
                )
            }
            db.withTransaction {
                dao.deleteAllLatestNews()
                dao.insertLatestNews(latestNews)
            }
        },
        shouldFetch = { cachedNews ->
            if (forceRefresh) { // to update news when the user swipe the refresh layout.
                true
            } else { // to update news everyday automatically
                val sortedNews = cachedNews.sortedBy {it.updatedAt}
                val oldestNews = sortedNews.firstOrNull()?.updatedAt
                val needsRefresh = oldestNews == null ||
                        oldestNews < System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
                needsRefresh
            }
        },
        onFetchFailed = {
            if (it !is HttpException && it !is IOException) {
                throw it
            }
            onFetchFailed(it)
        }
    )

    fun getForYouNews(
        country: String,
        forceRefresh: Boolean
    ) = networkBoundResource(
        query = { dao.getForYouNews() },
        fetch = { api.getForYouNews(country).articles },
        saveFetchResult = { articles ->
            val forYouNews = articles.map { news ->
                ForYouNewsEntity(
                    title = news.title,
                    url = news.url,
                    imageUrl = news.urlToImage,
                    publishedAt = news.publishedAt,
                    source = news.source.name,
                    desc = news.description,
                )
            }
            db.withTransaction {
                dao.deleteAllForYouNews()
                dao.insertForYouNews(forYouNews)
            }
        },
        shouldFetch = { cashedNews ->
            if (forceRefresh){
                true
            } else {
                val sortedNews = cashedNews.sortedBy { it.updatedAt }
                val oldestNews = sortedNews.firstOrNull()?.updatedAt
                val needsRefresh  = oldestNews == null ||
                        oldestNews > System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
                needsRefresh
            }
        }
    )

    fun getExploreNews(category: String) = networkBoundResourceApiOnly(
        fetch = {
            api.getExploreNews(category)
        }
    )

    fun getSearchNews(query: String) = networkBoundResourceApiOnly(
        fetch = {
            api.getSearchNews(query)
        }
    )

    suspend fun updateNews(latestNewsEntity: LatestNewsEntity) {
        dao.updateNews(latestNewsEntity)
    }

    fun getBookmarks(): Flow<List<LatestNewsEntity>> = dao.getBookmarkedNews()

}