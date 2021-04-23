package com.omarahmed.getnews2.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.omarahmed.getnews2.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    val searchQuery = MutableSharedFlow<String>()
    private val searchFlow = searchQuery.flatMapLatest {
        repository.getSearchNews(it)
    }
    val getSearchNews = searchFlow.asLiveData()
}