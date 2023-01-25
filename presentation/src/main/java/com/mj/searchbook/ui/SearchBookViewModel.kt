package com.mj.searchbook.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mj.domain.model.BookItem
import com.mj.domain.usecase.SearchBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SearchBookViewModel @Inject constructor(
    private val searchBookUseCase: SearchBookUseCase,
) : ViewModel() {

    private val _uiEventFlow = MutableSharedFlow<SearchUIEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    private val _bookList = MutableLiveData<MutableList<BookItem>>()
    val bookList: LiveData<MutableList<BookItem>> get() = _bookList

    private val _totalCount = MutableLiveData<Int>()
    val totalCount: LiveData<Int> get() = _totalCount

    private val _latestQuery = MutableLiveData<String>()

    fun requestBooks(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            Timber.d("query: $query, current offset: 0")
            if (query.isEmpty()) {
                eventTrigger(SearchUIEvent.Empty)
                return@launch
            }
            _latestQuery.postValue(query)
            searchBookUseCase.searchBook(query, 0)
                .onStart { eventTrigger(SearchUIEvent.Loading) }
                .onCompletion { eventTrigger(SearchUIEvent.Success) }
                .catch { error ->
                    Timber.e(error)
                    val event = when (error) {
                        is ConnectException, is UnknownHostException -> SearchUIEvent.Disconnect
                        else -> SearchUIEvent.Error(error.message)
                    }
                    eventTrigger(event)
                }.collect { books ->
                    _totalCount.postValue(books.totalCount)
                    _bookList.postValue(books.items.toMutableList())
                }
        }
    }

    fun requestPagingBooks(offset: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val query = _latestQuery.value ?: return@launch
            Timber.d("query: $query, current offset: $offset")
            searchBookUseCase.searchBook(query, offset)
                .onStart { eventTrigger(SearchUIEvent.Loading) }
                .onCompletion { eventTrigger(SearchUIEvent.Success) }
                .catch { error ->
                    Timber.e(error)
                    val event = when (error) {
                        is ConnectException, is UnknownHostException -> SearchUIEvent.Disconnect
                        else -> SearchUIEvent.Error(error.message)
                    }
                    eventTrigger(event)
                }.collect { books ->
                    val pagingBookList = _bookList.value ?: emptyList<BookItem>().toMutableList()
                    pagingBookList.addAll(books.items)
                    _bookList.postValue(pagingBookList)
                }
        }
    }

    private suspend fun eventTrigger(event: SearchUIEvent) {
        _uiEventFlow.emit(event)
    }

    sealed class SearchUIEvent {
        data class Error(val msg: String?) : SearchUIEvent()
        object Loading : SearchUIEvent()
        object Success : SearchUIEvent()
        object Empty : SearchUIEvent()
        object Disconnect : SearchUIEvent()
    }
}