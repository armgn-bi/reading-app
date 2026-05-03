package com.readingapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingapp.data.database.entities.Book
import com.readingapp.data.repository.BookRepository
import com.readingapp.utils.file.FileUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookListUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUploading: Boolean = false
)

class BookListViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookListUiState())
    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()

    init {
        loadBooks()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                bookRepository.getAllBooks().collect { books ->
                    _uiState.value = BookListUiState(books = books)
                }
            } catch (e: Exception) {
                _uiState.value = BookListUiState(error = e.message)
            }
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            try {
                bookRepository.deleteBook(book)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun loadBook(context: Context, bookUri: Uri, translationUri: Uri? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploading = true, error = null)
            try {
                val fileName = FileUtil.getFileName(context, bookUri) ?: "Bilinmeyen Kitap"
                val fileExtension = FileUtil.getFileExtension(fileName)
                val content = FileUtil.readTextFile(context, bookUri)
                val lines = content.lines().filter { it.isNotBlank() }

                val book = Book(
                    title = fileName,
                    filePath = bookUri.toString(),
                    fileType = fileExtension,
                    translationFilePath = translationUri?.toString(),
                    totalPages = lines.size,
                    currentPage = 0,
                    currentPosition = 0,
                    createdAt = System.currentTimeMillis(),
                    lastReadAt = null,
                    category = null
                )

                bookRepository.insertBook(book)
                _uiState.value = _uiState.value.copy(isUploading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUploading = false,
                    error = "Dosya yüklenirken hata: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun renameBook(book: Book) {
        viewModelScope.launch {
            try {
                bookRepository.updateBook(book)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
