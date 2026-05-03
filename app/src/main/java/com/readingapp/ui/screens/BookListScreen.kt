package com.readingapp.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.readingapp.data.database.entities.Book
import com.readingapp.viewmodel.BookListUiState
import com.readingapp.viewmodel.BookListViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    viewModel: BookListViewModel,
    onBookClick: (Long) -> Unit,
    onWordBookClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf<Book?>(null) }
    var showRenameDialog by remember { mutableStateOf<Book?>(null) }
    var showTranslationDialog by remember { mutableStateOf(false) }
    var selectedBookUri by remember { mutableStateOf<Uri?>(null) }
    var newBookName by remember { mutableStateOf("") }

    val bookPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedBookUri = it
            showTranslationDialog = true
        }
    }

    val translationPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedBookUri?.let { bookUri ->
            viewModel.loadBook(context, bookUri, uri)
            selectedBookUri = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kitaplar") },
                actions = {
                    IconButton(onClick = onWordBookClick) {
                        Icon(Icons.Default.MenuBook, contentDescription = "Kelime Defteri")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Ayarlar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { bookPickerLauncher.launch("text/*") }) {
                Icon(Icons.Default.Add, contentDescription = "Kitap Ekle")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isUploading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Kitap yükleniyor...")
                    }
                }
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Hata: ${uiState.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("Tamam")
                        }
                    }
                }
                uiState.books.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Henüz kitap yok",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Kitap eklemek için + butonuna tıklayın",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = paddingValues
                    ) {
                        items(uiState.books) { book ->
                            BookItem(
                                book = book,
                                onClick = { onBookClick(book.id) },
                                onDelete = { showDeleteDialog = book },
                                onRename = {
                                    showRenameDialog = book
                                    newBookName = book.title
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Kitabı Sil") },
            text = { Text("${showDeleteDialog?.title} kitabını silmek istediğine emin misin?") },
                            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog?.let { viewModel.deleteBook(it) }
                        showDeleteDialog = null
                    }
                ) {
                    Text("Sil")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("İptal")
                }
            }
        )
    }

    if (showRenameDialog != null) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = null },
            title = { Text("Kitap İsmi Değiştir") },
            text = {
                androidx.compose.material3.OutlinedTextField(
                    value = newBookName,
                    onValueChange = { newBookName = it },
                    label = { Text("Yeni İsim") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRenameDialog?.let { book ->
                            if (newBookName.isNotBlank()) {
                                viewModel.renameBook(book.copy(title = newBookName))
                            }
                        }
                        showRenameDialog = null
                    }
                ) {
                    Text("Kaydet")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = null }) {
                    Text("İptal")
                }
            }
        )
    }

    if (showTranslationDialog) {
        AlertDialog(
            onDismissRequest = {
                showTranslationDialog = false
                selectedBookUri = null
            },
            title = { Text("Çeviri Dosyası") },
            text = { Text("Paralel çeviri dosyası yüklemek ister misin? (İsteğe bağlı)") },
            confirmButton = {
                TextButton(
                    onClick = {
                        translationPickerLauncher.launch("text/*")
                        showTranslationDialog = false
                    }
                ) {
                    Text("Evet")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedBookUri?.let { viewModel.loadBook(context, it, null) }
                        selectedBookUri = null
                        showTranslationDialog = false
                    }
                ) {
                    Text("Hayır")
                }
            }
        )
    }
}

@Composable
fun BookItem(
    book: Book,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onRename: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    IconButton(onClick = onRename) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "İsim Değiştir",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Sil",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tür: ${book.fileType.uppercase()}",
                    style = MaterialTheme.typography.bodySmall
                )
                book.lastReadAt?.let {
                    Text(
                        text = formatDate(it),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            if (book.totalPages != null && book.totalPages > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                val progress = if (book.totalPages > 0) {
                    (book.currentPage.toFloat() / book.totalPages * 100).toInt()
                } else 0
                Text(
                    text = "İlerleme: $progress% (${book.currentPage}/${book.totalPages})",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (book.translationFilePath != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Çeviri dosyası mevcut",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
