package com.omarahmed.getnews2.data

import android.content.Context
import android.widget.Toast
import androidx.room.withTransaction
import com.omarahmed.getnews2.data.api.NewsApi
import com.omarahmed.getnews2.data.room.NewsDatabase
import com.omarahmed.getnews2.data.room.NewsEntity
import com.omarahmed.getnews2.util.Resource
import com.omarahmed.getnews2.util.networkBoundResource
import com.omarahmed.getnews2.util.networkBoundResourceApiOnly
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: NewsApi,
    private val db: NewsDatabase
) {
    private val dao = db.newsDao()

    fun getLatestNews(
        context: Context,
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<NewsEntity>>> = networkBoundResource(
        query = {
            Toast.makeText(context, "from database", Toast.LENGTH_SHORT).show()
            dao.getLatestNews()
        },
        fetch = {
            Toast.makeText(context, "from api", Toast.LENGTH_SHORT).show()
            api.getLatestNews().articles
        },
        saveFetchResult = { articles ->
            val bookmarkedNews = dao.getBookmarkedNews().first()
            val newsArticles = articles.map { serverNews ->
                val isBookmarked = bookmarkedNews.any { newsEntity ->
                    newsEntity.url == serverNews.url
                }
                NewsEntity(
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
                dao.insertLatestNews(newsArticles)
            }
        },
        shouldFetch = { cachedNews ->
            cachedNews.isEmpty() || forceRefresh
        },
        onFetchSuccess = onFetchSuccess,
        onFetchFailed = {
            if (it !is HttpException && it !is IOException){
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
            db.withTransaction {
                dao.deleteAllForYouNews()
                dao.insertForYouNews(articles)
            }
        },
        shouldFetch = {cashedNews ->
            forceRefresh || cashedNews.isEmpty()
        }
    )

    fun getExploreNews(category: String) = networkBoundResourceApiOnly(
        fetch = {
            api.getExploreNews(category)
        }
    )

    fun getSearchNews(query:String) = networkBoundResourceApiOnly(
        fetch = {
            api.getSearchNews(query)
        }
    )

    suspend fun updateNews(newsEntity: NewsEntity) {
        dao.updateNews(newsEntity)
    }

    fun getBookmarks(): Flow<List<NewsEntity>> = dao.getBookmarkedNews()

}