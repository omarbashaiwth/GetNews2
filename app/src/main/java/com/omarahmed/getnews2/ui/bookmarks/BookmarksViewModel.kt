package com.omarahmed.getnews2.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarahmed.getnews2.data.Repository
import com.omarahmed.getnews2.data.room.LatestNewsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {


    val getBookmarks = repository.getBookmarks().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        null
    )

    fun onUnBookmarkClick(newsEntity: LatestNewsEntity) {
       val currentBookmark = newsEntity.isBookmarked
       val updatedNews = newsEntity.copy(isBookmarked = !currentBookmark)
       viewModelScope.launch {
           repository.updateNews(updatedNews)
       }
    }

}