package com.omarahmed.getnews2.ui.home

import android.app.Application
import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.omarahmed.getnews2.data.Repository
import com.omarahmed.getnews2.data.room.NewsEntity
import com.omarahmed.getnews2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val app: Application
) : AndroidViewModel(app) {

    private val newsChannel = Channel<NewsEvents>()
    val newsEvents = newsChannel.receiveAsFlow()

    val getLatestNews = repository.getLatestNews(app).asLiveData()
    val getForYouNews = repository.getForYouNews(getCountry()).asLiveData()

    fun onBookmarkClick(newsEntity: NewsEntity) {
        val currentBookmarked = newsEntity.isBookmarked
        val updateNews = newsEntity.copy(isBookmarked = !currentBookmarked)
        viewModelScope.launch {
            repository.updateNews(updateNews)
            if (currentBookmarked) {
                newsChannel.send(NewsEvents.ShowBookmarkedMessage("unbookmarked"))
            } else {
                newsChannel.send(NewsEvents.ShowBookmarkedMessage("bookmarked"))
            }

        }
    }

    private fun getCountry(): String {
        val telephonyManager = app.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = telephonyManager.networkCountryIso.toUpperCase(Locale.ROOT)
        val countryName = Locale("", countryCode).displayName
        Log.d("countryCode", countryName)
        return countryName
    }

    sealed class NewsEvents {
        data class ShowBookmarkedMessage(val msg: String) : NewsEvents()
    }
}