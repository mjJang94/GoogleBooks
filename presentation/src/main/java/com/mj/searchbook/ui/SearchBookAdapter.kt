package com.mj.searchbook.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mj.domain.model.BookItem
import com.mj.searchbook.databinding.RowSearchBookBinding
import com.mj.searchbook.util.loadUrl
import com.mj.searchbook.util.setTextOrNonEmpty

class SearchBookAdapter(
    private val showDetail: (String) -> Unit,
) : ListAdapter<BookItem, SearchBookAdapter.SearchBookViewHolder>(comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBookViewHolder {
        val binding = RowSearchBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchBookViewHolder(binding.root) u@{ item ->
            with(binding) {
                title.setTextOrNonEmpty(item.title)
                author.setTextOrNonEmpty(item.authors)
                date.setTextOrNonEmpty(item.publishedDate)
                thumbnail.loadUrl(item.imageLinks)
                container.setOnClickListener {
                    showDetail.invoke(item.detailLink)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: SearchBookViewHolder, position: Int) {
        getItem(position)?.let {
            holder.update(it)
        }
    }

    inner class SearchBookViewHolder(itemView: View, val update: (BookItem) -> Unit) : RecyclerView.ViewHolder(itemView)

    companion object {
        val comparator = object : DiffUtil.ItemCallback<BookItem>() {
            override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem) = oldItem == newItem
        }
    }
}