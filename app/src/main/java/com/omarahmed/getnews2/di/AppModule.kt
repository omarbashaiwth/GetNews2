package com.omarahmed.getnews2.di

import android.app.Application
import androidx.room.Room
import com.omarahmed.getnews2.data.api.NewsApi
import com.omarahmed.getnews2.data.room.NewsDatabase
import com.omarahmed.getnews2.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)

    @Provides
    fun provideDatabase(
        app: Application
    ): NewsDatabase =
        Room.databaseBuilder(app,NewsDatabase::class.java,"news_database")
            .build()
}