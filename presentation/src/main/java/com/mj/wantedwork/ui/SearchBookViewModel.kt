package com.mj.wantedwork.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mj.domain.model.Book
import com.mj.domain.usecase.SearchBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val _searchFlow = MutableSharedFlow<Pair<String, Long>>()
    private val _search = _searchFlow.asLiveData()

    private val _uiEventFlow = MutableSharedFlow<SearchUIEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val booksDataFlow = _searchFlow
        .flatMapLatest { (query, currentIndex) ->
            Timber.e("query: $query", "currentIndex: $currentIndex")
            searchBooks(query, currentIndex)
        }.flowOn(Dispatchers.IO)
        .onStart { triggerUIEvent(SearchUIEvent.Loading) }
        .onCompletion { triggerUIEvent(SearchUIEvent.Success) }
        .catch { error ->
            Timber.e(error.message)
            triggerUIEvent(SearchUIEvent.Error(error.message))
        }


    fun search(query: String, currentIndex: Long) {
        launch {
            _searchFlow.emit(query to currentIndex)
        }
    }

    private suspend fun searchBooks(query: String, currentIndex: Long): Flow<Book> =
        searchBookUseCase.searchBook(query, currentIndex)

    private suspend fun triggerUIEvent(event: SearchUIEvent) {
        _uiEventFlow.emit(event)
    }

    sealed class SearchUIEvent {
        data class Error(val msg: String?) : SearchUIEvent()
        object Loading : SearchUIEvent()
        object Success : SearchUIEvent()
    }
}