package com.readingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingapp.data.database.entities.Book
import com.readingapp.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReaderUiState(
    val book: Book? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ReaderViewModel(
    private val bookRepository: BookRepository,
    private val bookId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    init {
        loadBook()
    }

    private fun loadBook() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                bookRepository.getBookById(bookId).collect { book ->
                    _uiState.value = ReaderUiState(book = book)
                }
            } catch (e: Exception) {
                _uiState.value = ReaderUiState(error = e.message)
            }
        }
    }

    fun updateProgress(page: Int, position: Int) {
        viewModelScope.launch {
            try {
                val book = _uiState.value.book ?: return@launch
                bookRepository.updateReadingProgress(book.id, page, position, System.currentTimeMillis())
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
