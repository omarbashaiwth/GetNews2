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
import kotlinx.coroutines.flow.*
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

    private val refreshTriggerChannel = Channel<Refresh>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()
    var pendingScrollToTopAfterRefresh = false

    val news = refreshTrigger.flatMapLatest { refresh ->
        val latestNews = repository.getLatestNews(
            context = app,
            forceRefresh = refresh == Refresh.FORCE,
            onFetchSuccess = {
                pendingScrollToTopAfterRefresh = true
            },
            onFetchFailed = {
                viewModelScope.launch { newsChannel.send(NewsEvents.ShowErrorMessage(it)) }
            }
        )
        val forYouNews = repository.getForYouNews(
            country = getCountry(),
            refresh == Refresh.FORCE
        )
        combine(latestNews, forYouNews) { latest, forYou ->
            Pair(latest, forYou)
        }
    }.asLiveData()

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

    fun onManualRefresh() {
        if (news.value !is Resource.Loading<*>) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Refresh.FORCE)
            }
        }
    }

    fun onStart() {
        if (news.value !is Resource.Loading<*>) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Refresh.NORMAL)
            }
        }
    }

    enum class Refresh {
        FORCE, NORMAL
    }

    sealed class NewsEvents {
        data class ShowBookmarkedMessage(val msg: String) : NewsEvents()
        data class ShowErrorMessage(val error: Throwable) : NewsEvents()
    }
}