package com.omarahmed.getnews2.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(TypeConverter::class)
@Database(entities = [NewsEntity::class] , version = 1)
abstract class NewsDatabase : RoomDatabase(){

    abstract fun newsDao(): NewsDao
}