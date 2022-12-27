package com.mj.wantedwork.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.mj.domain.model.BookItem
import com.mj.wantedwork.R
import com.mj.wantedwork.databinding.RowSearchBookBinding

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
        fun bind(item: BookItem){
            with(binding){
                title.text = item.title
                author.text = item.authors
                date.text = item.publishedDate

                Glide.with(thumbnail.context)
                    .load(item.imageLinks)
                    .skipMemoryCache(false)
                    .error(R.drawable.image_not_supported)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .into(thumbnail)

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