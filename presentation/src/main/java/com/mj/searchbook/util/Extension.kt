package com.mj.searchbook.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.mj.searchbook.R

fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
    this?.let { Toast.makeText(it, text, duration).show() }

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun RecyclerView.setLinearEndlessScroll(execute: (Int) -> Unit) {
    val layoutManager = this.layoutManager as LinearLayoutManager
    this.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            execute(totalItemsCount + 20)
        }
    })
}

fun AppCompatImageView.loadUrl(url: String) {
    val error = Glide.with(this.context)
        .load(R.drawable.image_not_supported)
        .fitCenter()
    Glide.with(this.context)
        .load(url)
        .skipMemoryCache(false)
        .error(error)
        .fitCenter()
        .format(DecodeFormat.PREFER_RGB_565)
        .into(this)
}

fun AppCompatTextView.setTextOrNonEmpty(text: String?) {
    val tmpText = if (text.isNullOrEmpty()) "정보 없음" else text
    this.text = tmpText
}
