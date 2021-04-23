package com.omarahmed.getnews2.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_preferences")

    private object PreferencesKeys{
        val CURRENT_POSITION = intPreferencesKey("position")
    }

    val preferencesFlow: Flow<Int> = context.dataStore.data
        .catch {
            if (it is IOException){
                Log.d("PreferencesManager","error",it)
            } else {
                throw it
            }
        }
        .map {
             it[PreferencesKeys.CURRENT_POSITION] ?: 0
        }

    suspend fun updatePosition(position: Int){
        context.dataStore.edit {
            it[PreferencesKeys.CURRENT_POSITION] = position
        }
    }
}


