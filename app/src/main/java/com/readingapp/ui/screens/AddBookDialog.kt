package com.readingapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.readingapp.utils.file.FileUtils

@Composable
fun AddBookDialog(
    onDismiss: () -> Unit,
    onBookAdded: (title: String, bookUri: Uri, translationUri: Uri?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var bookUri by remember { mutableStateOf<Uri?>(null) }
    var translationUri by remember { mutableStateOf<Uri?>(null) }
    var bookFileName by remember { mutableStateOf("") }
    var translationFileName by remember { mutableStateOf("") }
    val context = LocalContext.current

    val bookPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            bookUri = it
            bookFileName = FileUtils.getFileName(context = context, uri = it)
            if (title.isEmpty()) {
                title = bookFileName.substringBeforeLast(".")
            }
        }
    }

    val translationPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            translationUri = it
            translationFileName = FileUtils.getFileName(context = context, uri = it)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Yeni Kitap Ekle",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Kitap Adı") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Kitap Dosyası Seçimi
                OutlinedButton(
                    onClick = { bookPickerLauncher.launch(arrayOf("text/*")) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (bookFileName.isEmpty()) "Kitap Dosyası Seç" else bookFileName)
                }

                // Çeviri Dosyası Seçimi (Opsiyonel)
                OutlinedButton(
                    onClick = { translationPickerLauncher.launch(arrayOf("text/*")) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (translationFileName.isEmpty()) "Çeviri Dosyası Seç (Opsiyonel)" else translationFileName)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("İptal")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank() && bookUri != null) {
                                onBookAdded(title, bookUri!!, translationUri)
                            }
                        },
                        enabled = title.isNotBlank() && bookUri != null
                    ) {
                        Text("Ekle")
                    }
                }
            }
        }
    }
}
