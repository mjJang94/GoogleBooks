package com.mj.wantedwork.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mj.domain.model.BookItem
import com.mj.domain.usecase.SearchBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class SearchBookViewModel @Inject constructor(
    private val searchBookUseCase: SearchBookUseCase,
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    private val _uiEventFlow = MutableSharedFlow<SearchUIEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    private val _bookList = MutableLiveData<MutableList<BookItem>>()
    val bookList: LiveData<MutableList<BookItem>> get() = _bookList

    private val _totalCount = MutableLiveData<Int>()
    val totalCount: LiveData<Int> get() = _totalCount

    private val _latestQuery = MutableLiveData<String>()

    fun requestBooks(query: String) {
        launch(Dispatchers.Default) {
            Timber.d("query: $query, current offset: 0")
            if (query.isEmpty()) {
                eventTrigger(SearchUIEvent.Empty)
                return@launch
            }
            _latestQuery.postValue(query)
            searchBookUseCase.searchBook(query, 0)
                .flowOn(Dispatchers.IO)
                .onStart { eventTrigger(SearchUIEvent.Loading) }
                .onCompletion { eventTrigger(SearchUIEvent.Success) }
                .catch { error ->
                    Timber.e(error.message)
                    eventTrigger(SearchUIEvent.Error(error.message))
                }.collect { books ->
                    _totalCount.postValue(books.totalCount)
                    _bookList.postValue(books.items.toMutableList())
                }
        }
    }

    fun requestPagingBooks(offset: Int) {
        launch(Dispatchers.Default) {
            val query = _latestQuery.value ?: return@launch
            Timber.d("query: $query, current offset: $offset")
            searchBookUseCase.searchBook(query, offset)
                .flowOn(Dispatchers.IO)
                .onStart { eventTrigger(SearchUIEvent.Loading) }
                .onCompletion { eventTrigger(SearchUIEvent.Success) }
                .catch { error ->
                    Timber.e(error.message)
                    eventTrigger(SearchUIEvent.Error(error.message))
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
        object Empty: SearchUIEvent()
    }
}