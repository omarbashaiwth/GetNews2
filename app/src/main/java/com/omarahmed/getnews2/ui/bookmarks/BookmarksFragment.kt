package com.omarahmed.getnews2.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.omarahmed.getnews2.R
import com.omarahmed.getnews2.databinding.FragmentBookmarksBinding
import com.omarahmed.getnews2.util.showNews
import com.omarahmed.getnews2.util.showShareBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {

    private val bookmarksViewModel: BookmarksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBookmarksBinding.bind(view)
        val bookmarksAdapter = BookmarksAdapter(
            onItemClick = {showNews(it.url)},
            onBookmarkClick = { newsEntity ->
                bookmarksViewModel.onUnBookmarkClick(newsEntity)
            },
            onShareClick = {showShareBottomSheet(it.url)}
        )

        binding.apply {
            rvBookmarks.apply {
                adapter = bookmarksAdapter
                layoutManager = GridLayoutManager(requireContext(),2)
                setHasFixedSize(true)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                bookmarksViewModel.getBookmarks.collect {
                    bookmarksAdapter.submitList(it ?: return@collect)

                    ivNoBookmarks.isVisible = it.isNullOrEmpty()
                    tvError.isVisible = it.isNullOrEmpty()
                    tvTitle.isVisible = it.isNullOrEmpty()
                }
            }
        }
    }
}