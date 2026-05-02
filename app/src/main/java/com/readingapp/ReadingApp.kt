package com.readingapp

import android.content.Context
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
import com.readingapp.utils.file.FileUtils
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReadingApp() {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedBookId by remember { mutableStateOf<Long?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val bookRepository: BookRepository = org.koin.compose.koinInject()
    val context = LocalContext.current

    BookListScreen(
        onBookClick = { bookId ->
            selectedBookId = bookId
            // TODO: ReaderScreen'e geçiş yapılacak
        },
        onAddBookClick = {
            showAddDialog = true
        }
    )

    if (showAddDialog) {
        AddBookDialog(
            onDismiss = { showAddDialog = false },
            onBookAdded = { title, bookUri, translationUri ->
                scope.launch {
                    val book = Book(
                        title = title,
                        filePath = bookUri.toString(),
                        fileType = FileUtils.getFileExtension(FileUtils.getFileName(context, bookUri)),
                        translationFilePath = translationUri?.toString()
                    )
                    bookRepository.insertBook(book)
                    showAddDialog = false
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
