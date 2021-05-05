package com.omarahmed.getnews2.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(TypeConverter::class)
@Database(entities = [LatestNewsEntity::class, ForYouNewsEntity::class] , version = 1)
abstract class NewsDatabase : RoomDatabase(){

    abstract fun newsDao(): NewsDao
}