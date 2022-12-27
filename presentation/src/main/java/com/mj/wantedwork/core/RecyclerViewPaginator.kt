package com.mj.wantedwork.core

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE


abstract class RecyclerViewCustomPaging(recyclerView: RecyclerView) : RecyclerView.OnScrollListener() {

    private val batchSize = 19L
    private var currentPage: Long = 0L
    private val threshold = 2
    private var endWithAuto = false

    private val layoutManager: RecyclerView.LayoutManager?

    private val currentSize: Long
        get() = ++currentPage

    abstract val isLastPage: Boolean

    init {
        recyclerView.addOnScrollListener(this)
        this.layoutManager = recyclerView.layoutManager
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == SCROLL_STATE_IDLE) {
            val visibleItemCount = layoutManager!!.childCount
            val totalItemCount = layoutManager.itemCount

            var firstVisibleItemPosition = 0
            if (layoutManager is LinearLayoutManager) {
                firstVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

            }

            if (isLastPage) return

            if (visibleItemCount + firstVisibleItemPosition + threshold >= totalItemCount) {
                if (!endWithAuto) {
                    endWithAuto = true
                    loadMore(currentSize)
                }
            } else {
                endWithAuto = false
            }
        }
    }

    fun reset() {
        currentPage = 0L
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
    }

    abstract fun loadMore(currentSize: Long)
}