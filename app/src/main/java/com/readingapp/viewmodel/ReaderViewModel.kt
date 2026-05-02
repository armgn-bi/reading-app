package com.readingapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingapp.data.database.entities.Book
import com.readingapp.data.repository.BookRepository
import com.readingapp.utils.file.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReaderUiState(
    val book: Book? = null,
    val content: List<String> = emptyList(),
    val translationContent: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val fontSize: Int = 16,
    val isDarkTheme: Boolean = false,
    val selectedSentenceIndex: Int? = null
)

class ReaderViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    fun loadBook(bookId: Long, context: Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                bookRepository.getBookById(bookId).collect { book ->
                    book?.let {
                        _uiState.value = _uiState.value.copy(
                            book = it,
                            isLoading = false
                        )
                        loadContent(it, context)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun loadContent(book: Book, context: Context) {
        viewModelScope.launch {
            try {
                val bookUri = android.net.Uri.parse(book.filePath)

                // URI permission'ını almayı dene
                val permissionGranted = FileUtils.takePersistableUriPermission(context, bookUri)
                if (!permissionGranted) {
                    _uiState.value = _uiState.value.copy(error = "Dosya erişim izni reddedildi. Lütfen kitabı tekrar ekleyin.")
                    return@launch
                }

                val content = FileUtils.readTextFile(context, bookUri)
                val contentLines = content.split("\n")

                var translationLines = emptyList<String>()
                book.translationFilePath?.let { translationPath ->
                    val translationUri = android.net.Uri.parse(translationPath)

                    // Çeviri dosyası için de permission almayı dene
                    val translationPermissionGranted = FileUtils.takePersistableUriPermission(context, translationUri)
                    if (!translationPermissionGranted) {
                        _uiState.value = _uiState.value.copy(error = "Çeviri dosyası erişim izni reddedildi.")
                        return@launch
                    }

                    val translationContent = FileUtils.readTextFile(context, translationUri)
                    translationLines = translationContent.split("\n")
                }

                _uiState.value = _uiState.value.copy(
                    content = contentLines,
                    translationContent = translationLines
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "İçerik yüklenemedi: ${e.message}")
            }
        }
    }

    fun updateFontSize(size: Int) {
        _uiState.value = _uiState.value.copy(fontSize = size)
    }

    fun toggleTheme() {
        _uiState.value = _uiState.value.copy(isDarkTheme = !_uiState.value.isDarkTheme)
    }

    fun selectSentence(index: Int) {
        _uiState.value = _uiState.value.copy(selectedSentenceIndex = index)
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedSentenceIndex = null)
    }

    fun saveReadingProgress(bookId: Long, position: Int) {
        viewModelScope.launch {
            try {
                bookRepository.updateReadingProgress(bookId, 0, position)
                bookRepository.updateLastReadAt(bookId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
