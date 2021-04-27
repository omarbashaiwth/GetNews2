package com.omarahmed.getnews2.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.omarahmed.getnews2.R
import com.omarahmed.getnews2.databinding.FragmentHomeBinding
import com.omarahmed.getnews2.util.Resource
import com.omarahmed.getnews2.util.showNews
import com.omarahmed.getnews2.util.showShareBottomSheet
import com.omarahmed.getnews2.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import java.util.*
import kotlin.math.abs

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
                val compositePaTransformer = CompositePageTransformer()
                compositePaTransformer.apply {
                    addTransformer(MarginPageTransformer(30))
                    addTransformer { page, position ->
                        val r = 1 - abs(position)
                        page.scaleY = 0.85f + r * 0.14f
                    }
                }
                adapter = viewPagerAdapter
                offscreenPageLimit = 3
                getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                setPageTransformer(compositePaTransformer)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                homeViewModel.getLatestNews.collect {
                    val result = it ?:return@collect
                    swipeRefreshLayout.isRefreshing = result is Resource.Loading
                    progressBar.isVisible = result is Resource.Loading && result.data.isNullOrEmpty()
                    tvLatestNewsTitle.isVisible = result is Resource.Loading || !result.data.isNullOrEmpty()

                    latestNewsAdapter.submitList(result.data){
                        if (homeViewModel.pendingScrollToTopAfterRefresh){
                            rvLatestNews.scrollToPosition(0)
                            homeViewModel.pendingScrollToTopAfterRefresh = false
                        }
                    }
                }

            }
            swipeRefreshLayout.setOnRefreshListener {
                homeViewModel.onManualRefresh()
            }
            homeViewModel.getForYouNews.observe(viewLifecycleOwner) {
                val result = it ?: return@observe
                viewPagerAdapter.submitList(result.data)
                tvForYouTitle.isVisible = result is Resource.Loading || !result.data.isNullOrEmpty()

            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            homeViewModel.newsEvents.collect { event ->
                when (event) {
                    is HomeViewModel.NewsEvents.ShowBookmarkedMessage -> {
                        showSnackBar(message = event.msg)
                    }
                    is HomeViewModel.NewsEvents.ShowErrorMessage ->{
                        showSnackBar(event.error.localizedMessage ?:"Unknown error occurred")
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

