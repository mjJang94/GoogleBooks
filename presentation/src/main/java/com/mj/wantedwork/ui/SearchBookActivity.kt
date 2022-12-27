package com.mj.wantedwork.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.mj.wantedwork.R
import com.mj.wantedwork.databinding.ActivitySearchBinding
import com.mj.wantedwork.ui.SearchBookViewModel.SearchUIEvent.*
import com.mj.wantedwork.util.hideKeyboard
import com.mj.wantedwork.util.setGone
import com.mj.wantedwork.util.setVisible
import com.mj.wantedwork.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SearchBookActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = lifecycleScope.coroutineContext

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: SearchBookAdapter
    private val viewModel: SearchBookViewModel by viewModels()

    private var currentIndex = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        initView()
        observeData()
    }

    private fun initViewBinding() {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initView() {

        adapter = SearchBookAdapter { link ->
            openDetailLink(link)
        }

        binding.rcyBooks.adapter = adapter
        binding.rcyBooks.setHasFixedSize(true)
        binding.rcyBooks.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.rcyBooks.canScrollVertically(5)){
                    val query = binding.input.text.toString().trim()
                    currentIndex += 20
                    viewModel.search(query, currentIndex)
                }
            }
        })
//        binding.rcyBooks.addOnScrollListener(object: RecyclerViewPaginator(binding.rcyBooks){
//            override val isLastPage: Boolean
//                get() = false
//
//            override fun loadMore(currentSize: Long) {
//                val query = binding.input.text.toString().trim()
//                viewModel.search(query, currentSize)
//            }
//        })

        binding.search.setOnClickListener {
            val query = binding.input.text.toString().trim()
            currentIndex = 0

            when (query.isEmpty()) {
                true -> toast("검색어를 입력해주세요.")
                else -> {
                    it.hideKeyboard()
                    viewModel.search(query, currentIndex)
                }
            }
        }
    }

    private fun observeData() {
        launch {
            viewModel.booksDataFlow.collectLatest { book ->
                binding.total.text = getString(R.string.search_result_counts, book.totalCount)
                adapter.submitList(book.items)
            }
        }

        launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEventFlow.collect { event ->
                    when (event) {
                        is Loading -> onLoading()
                        is Success -> onSuccess()
                        is Error -> onError(event.msg)
                    }
                }
            }
        }
    }

    private fun onLoading() = with(binding) {
        progress.setVisible()
    }

    private fun onError(msg: String?) = with(binding) {
        progress.setGone()
        toast(msg ?: "알 수 없는 에러가 발생했습니다.")
    }

    private fun onSuccess() = with(binding) {
        progress.setGone()
    }

    private fun openDetailLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
        }
        startActivity(intent)
    }
}