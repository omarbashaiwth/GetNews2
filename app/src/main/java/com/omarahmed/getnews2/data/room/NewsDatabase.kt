package com.omarahmed.getnews2.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omarahmed.getnews2.data.api.Article

@TypeConverters(TypeConverter::class)
@Database(entities = [NewsEntity::class, Article::class] , version = 1)
abstract class NewsDatabase : RoomDatabase(){

    abstract fun newsDao(): NewsDao
}