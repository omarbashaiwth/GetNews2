package com.omarahmed.getnews2.data.room

import androidx.room.TypeConverter
import com.omarahmed.getnews2.data.api.Source


class TypeConverter {

    @TypeConverter
    fun sourceToString(source: Source): String? = source.name

    @TypeConverter
    fun stringToSource(name: String): Source = Source(name)
}