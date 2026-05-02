package com.readingapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.readingapp.data.database.entities.Book
import com.readingapp.data.repository.BookRepository
import com.readingapp.ui.screens.AddBookDialog
import com.readingapp.ui.screens.BookListScreen
import com.readingapp.ui.screens.ReaderScreen
import com.readingapp.ui.screens.SettingsScreen
import com.readingapp.utils.file.FileUtils
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

enum class Screen {
    BookList,
    Reader,
    Settings
}

@Composable
fun ReadingApp() {
    var showAddDialog by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf(Screen.BookList) }
    var selectedBookId by remember { mutableStateOf<Long?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isDarkTheme by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bookRepository: BookRepository = org.koin.compose.koinInject()
    val context = LocalContext.current

    when (currentScreen) {
        Screen.BookList -> {
            BookListScreen(
                onBookClick = { bookId ->
                    selectedBookId = bookId
                    currentScreen = Screen.Reader
                },
                onAddBookClick = {
                    showAddDialog = true
                },
                onSettingsClick = {
                    currentScreen = Screen.Settings
                }
            )
        }
        Screen.Reader -> {
            if (selectedBookId != null) {
                ReaderScreen(
                    bookId = selectedBookId!!,
                    onBack = {
                        currentScreen = Screen.BookList
                        selectedBookId = null
                    }
                )
            }
        }
        Screen.Settings -> {
            SettingsScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = {
                    isDarkTheme = !isDarkTheme
                },
                onBack = {
                    currentScreen = Screen.BookList
                }
            )
        }
    }

    if (showAddDialog) {
        AddBookDialog(
            onDismiss = { showAddDialog = false },
            onBookAdded = { title, bookUri, translationUri ->
                scope.launch {
                    try {
                        // URI'yi kalıcı hale getir
                        val bookUriPersisted = FileUtils.takePersistableUriPermission(context, bookUri)
                        var translationUriPersisted = false
                        translationUri?.let {
                            translationUriPersisted = FileUtils.takePersistableUriPermission(context, it)
                        }

                        if (!bookUriPersisted) {
                            errorMessage = "Kitap dosyası için kalıcı izin alınamadı. Lütfenin tekrar deneyin."
                            showAddDialog = false
                            return@launch
                        }

                        val book = Book(
                            title = title,
                            filePath = bookUri.toString(),
                            fileType = FileUtils.getFileExtension(FileUtils.getFileName(context, bookUri)),
                            translationFilePath = translationUri?.toString(),
                            isUriPersisted = bookUriPersisted
                        )
                        bookRepository.insertBook(book)
                        showAddDialog = false
                    } catch (e: Exception) {
                        errorMessage = "Kitap eklenirken hata: ${e.message}"
                    }
                }
            }
        )
    }

    errorMessage?.let { error ->
        Snackbar(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(error)
        }
        LaunchedEffect(error) {
            kotlinx.coroutines.delay(3000)
            errorMessage = null
        }
    }
}
