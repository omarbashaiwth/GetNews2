package com.omarahmed.getnews2.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.omarahmed.getnews2.R
import com.omarahmed.getnews2.data.api.Article
import com.omarahmed.getnews2.databinding.FragmentSearchBinding
import com.omarahmed.getnews2.util.Resource
import com.omarahmed.getnews2.util.onQueryTextSubmit
import com.omarahmed.getnews2.util.showNews
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), SearchAdapter.OnItemClickListener {
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSearchBinding.bind(view)
        val searchAdapter = SearchAdapter(this)

        binding.apply {
            svSearch.apply {
                onActionViewExpanded()
                onQueryTextSubmit { query ->
                    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                        searchViewModel.searchQuery.emit(query)
                    }
                    clearFocus()
                }
            }
            rvSearch.adapter = searchAdapter
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            searchViewModel.getSearchNews.observe(viewLifecycleOwner) {
                val result = it ?: return@observe
                searchAdapter.submitList(result.data?.articles)

                pbSearch.isVisible = result is Resource.Loading

                ivSearch.isVisible =
                    (result.data?.articles.isNullOrEmpty() && result is Resource.Success) || result is Resource.Error
                tvTitleSearch.isVisible =
                    (result.data?.articles.isNullOrEmpty() && result is Resource.Success) || result is Resource.Error
                tvDescSearch.isVisible =
                    (result.data?.articles.isNullOrEmpty() && result is Resource.Success) || result is Resource.Error

                if (result is Resource.Error) {
                    tvTitleSearch.text = getString(R.string.something_went_wrong)
                    tvDescSearch.text = result.error?.localizedMessage

                } else {
                    tvTitleSearch.text = getString(R.string.not_found)
                    tvDescSearch.text = getString(R.string.search_desc)
                }
            }
        }
    }

    override fun onItemClick(article: Article) {
        showNews(article.url)
    }
}