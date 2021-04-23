package com.omarahmed.getnews2.ui.explore

import android.util.Log
import androidx.lifecycle.*
import com.omarahmed.getnews2.R
import com.omarahmed.getnews2.data.PreferencesManager
import com.omarahmed.getnews2.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val repository: Repository
) : ViewModel(
) {

    val categories = arrayListOf(
        Categories("BUSINESS", R.drawable.ic__business),
        Categories("TECHNOLOGY", R.drawable.ic__tech),
        Categories("HEALTH", R.drawable.ic__health),
        Categories("SCIENCE", R.drawable.ic__science),
        Categories("ENTERTAINMENT", R.drawable.ic__entertainment),
        Categories("SPORT", R.drawable.ic__sport)
    )

    private val positionFlow = preferencesManager.preferencesFlow
    val position = positionFlow.asLiveData()


    fun getExploreNews(category: String) = repository.getExploreNews(category).asLiveData()


    fun onUpdatePosition(position: Int) = viewModelScope.launch {
        preferencesManager.updatePosition(position)
    }

    fun onGetCategory(position: Int): String {
        return categories[position].title.toLowerCase(Locale.ROOT)
    }


}





