package com.mj.searchbook.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mj.searchbook.R
import com.mj.searchbook.databinding.ActivitySearchBinding
import com.mj.searchbook.ui.SearchBookViewModel.SearchUIEvent.*
import com.mj.searchbook.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchBookActivity : AppCompatActivity() {

    private val viewModel: SearchBookViewModel by viewModels()
    private val binding: ActivitySearchBinding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val adapter: SearchBookAdapter by lazy {
        SearchBookAdapter { link ->
            openDetailLink(link)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        observeData()
    }

    private fun initViews() {

        binding.rcyBooks.adapter = adapter
        binding.rcyBooks.setHasFixedSize(true)
        binding.rcyBooks.setLinearEndlessScroll { currentSize ->
            viewModel.requestPagingBooks(currentSize)
        }

        binding.search.setOnClickListener {
            val query = binding.input.text.toString().trim()
            viewModel.requestBooks(query)
            it.hideKeyboard()
        }
    }

    private fun observeData() {

        viewModel.bookList.observe(this) { bookItem ->
            /**
             *  if (newList == mList) {
             * // nothing to do (Note - still had to inc generation, since may have ongoing work)
             *   if (commitCallback != null) {
             *      commitCallback.run();
             *    }
             *   return;
             * }
             */
            adapter.submitList(bookItem.toMutableList())
        }

        viewModel.totalCount.observe(this) { totalCount ->
            binding.total.text = getString(R.string.search_result_counts, totalCount)
        }

        lifecycleScope.launch {
            viewModel.uiEventFlow.collect { event ->
                when (event) {
                    is Loading -> onLoading()
                    is Success -> onSuccess()
                    is Empty -> onEmptyQuery()
                    is Disconnect -> onNetworkDisconnected()
                    is Error -> onError(event.msg)
                }
            }
        }
    }

    private fun onLoading() = with(binding) {
        progress.setVisible()
    }

    private fun onError(msg: String?) = with(binding) {
        progress.setGone()
        toast(msg ?: getString(R.string.unknown_error_msg))
    }

    private fun onEmptyQuery() {
        toast(getString(R.string.insert_query))
    }

    private fun onSuccess() = with(binding) {
        progress.setGone()
    }

    private fun onNetworkDisconnected() {
        toast(getString(R.string.check_network))
    }

    private fun openDetailLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
        }
        startActivity(intent)
    }
}