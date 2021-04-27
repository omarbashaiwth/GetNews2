package com.omarahmed.getnews2.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.omarahmed.getnews2.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    val searchQuery = MutableSharedFlow<String>()
    private val searchFlow = searchQuery.flatMapLatest {
        repository.getSearchNews(it)
    }.stateIn(viewModelScope, SharingStarted.Lazily,null)
    val getSearchNews = searchFlow.asLiveData()
}