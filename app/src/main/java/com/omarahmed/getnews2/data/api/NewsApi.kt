package com.omarahmed.getnews2.data.api

import com.omarahmed.getnews2.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("/v2/top-headlines?apiKey=$API_KEY&language=en")
    suspend fun getLatestNews(): NewsResponse

    @GET("/v2/top-headlines?apiKey=$API_KEY&language=en")
    suspend fun getForYouNews(
        @Query("q") country: String? = "US"
    ): NewsResponse

    @GET("/v2/top-headlines?apiKey=$API_KEY&language=en")
    suspend fun getExploreNews(
        @Query("category") category: String
    ): Response<NewsResponse>

    @GET("/v2/top-headlines?apiKey=$API_KEY&language=en")
    suspend fun getSearchNews(
        @Query("q") query: String
    ): Response<NewsResponse>

}