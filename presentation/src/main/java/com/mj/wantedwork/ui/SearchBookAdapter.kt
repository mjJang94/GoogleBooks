package com.mj.wantedwork.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mj.domain.model.BookItem
import com.mj.wantedwork.databinding.RowSearchBookBinding
import com.mj.wantedwork.util.loadUrl
import com.mj.wantedwork.util.setTextOrNonEmpty

class SearchBookAdapter(
    private val showDetail: (String) -> Unit,
) : ListAdapter<BookItem, SearchBookAdapter.SearchBookViewHolder>(comparator) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBookViewHolder {
        return SearchBookViewHolder(RowSearchBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchBookViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class SearchBookViewHolder(private val binding: RowSearchBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookItem) {
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

    companion object {
        val comparator = object : DiffUtil.ItemCallback<BookItem>() {
            override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem) = oldItem == newItem
        }
    }
}