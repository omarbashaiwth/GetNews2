package com.omarahmed.getnews2.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.omarahmed.getnews2.R
import com.omarahmed.getnews2.databinding.FragmentHomeBinding
import com.omarahmed.getnews2.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHomeBinding.bind(view)
        val latestNewsAdapter = NewsAdapter(
            onItemClick = { showNews(it.url) },
            onBookmarkClick = { newsEntity ->
                homeViewModel.onBookmarkClick(newsEntity)
            },
            onShareClick = { showShareBottomSheet(it.url) }
        )

        val viewPagerAdapter = ViewPagerAdapter(
            onItemClick = { showNews(it.url) }
        )

        binding.apply {
            rvLatestNews.apply {
                adapter = latestNewsAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
            vpForYouNews.apply {
                adapter = viewPagerAdapter
                setPageTransformer(3,0)
            }
            swipeRefreshLayout.setOnRefreshListener {
                homeViewModel.onManualRefresh()
            }

            homeViewModel.news.observe(viewLifecycleOwner) {
                val latestNews = it.first
                swipeRefreshLayout.isRefreshing = latestNews is Resource.Loading
                latestNewsAdapter.submitList(latestNews.data)

                val forYouNews = it.second
                viewPagerAdapter.submitList(forYouNews.data)
            }

        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            homeViewModel.newsEvents.collect { event ->
                when (event) {
                    is HomeViewModel.NewsEvents.ShowBookmarkedMessage -> {
                        showToastMessage(message = event.msg)
                    }
                    is HomeViewModel.NewsEvents.ShowErrorMessage -> {
                        showToastMessage(event.error.localizedMessage ?: "Unknown error occurred")
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.onStart()
    }

}

