package com.readingapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.readingapp.viewmodel.ReaderUiState
import com.readingapp.viewmodel.ReaderViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    bookId: Long,
    onBack: () -> Unit
) {
    val viewModel: ReaderViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId, context)
    }

    // Okuma ilerlemesini kaydet
    LaunchedEffect(listState.firstVisibleItemIndex) {
        if (listState.firstVisibleItemIndex > 0) {
            viewModel.saveReadingProgress(bookId, listState.firstVisibleItemIndex)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.book?.title ?: "Okuma") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    // Yazı boyutu azaltma
                    IconButton(
                        onClick = { if (uiState.fontSize > 12) viewModel.updateFontSize(uiState.fontSize - 2) }
                    ) {
                        Icon(Icons.Default.TextDecrease, contentDescription = "Yazı küçült")
                    }
                    // Yazı boyutu artırma
                    IconButton(
                        onClick = { if (uiState.fontSize < 32) viewModel.updateFontSize(uiState.fontSize + 2) }
                    ) {
                        Icon(Icons.Default.TextIncrease, contentDescription = "Yazı büyüt")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(if (uiState.isDarkTheme) Color(0xFF121212) else Color.White)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.content.isEmpty() && !uiState.isLoading -> {
                    Text(
                        "İçerik yüklenemedi",
                        modifier = Modifier.align(Alignment.Center),
                        color = if (uiState.isDarkTheme) Color.White else Color.Black
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Ana metin
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(uiState.content) { index, line ->
                                if (line.isNotBlank()) {
                                    val isSelected = uiState.selectedSentenceIndex == index

                                    TextLine(
                                        text = line,
                                        fontSize = uiState.fontSize.sp,
                                        isSelected = isSelected,
                                        isDarkTheme = uiState.isDarkTheme,
                                        onClick = { viewModel.selectSentence(index) }
                                    )
                                }
                            }
                        }

                        // Seçili cümle ve çeviri alanı
                        uiState.selectedSentenceIndex?.let { selectedIndex ->
                            if (selectedIndex >= 0 && selectedIndex < uiState.content.size) {
                                val selectedSentence = uiState.content[selectedIndex]
                                val translation = if (selectedIndex < uiState.translationContent.size) {
                                    uiState.translationContent[selectedIndex]
                                } else {
                                    ""
                                }

                                SelectedSentenceCard(
                                    sentence = selectedSentence,
                                    translation = translation,
                                    fontSize = uiState.fontSize.sp,
                                    isDarkTheme = uiState.isDarkTheme,
                                    onClose = { viewModel.clearSelection() }
                                )
                            }
                        }
                    }
                }
            }

            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Text(error)
                }
                LaunchedEffect(error) {
                    kotlinx.coroutines.delay(3000)
                    viewModel.clearError()
                }
            }
        }
    }
}

@Composable
private fun TextLine(
    text: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    isSelected: Boolean,
    isDarkTheme: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        if (isDarkTheme) {
            Color(0xFF37474F) // Koyu tema için daha koyu arka plan
        } else {
            Color(0xFFE3F2FD) // Açık tema için açık mavi
        }
    } else {
        Color.Transparent
    }

    val textColor = if (isDarkTheme) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        // Ana metin
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontSize,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                color = textColor
            )
        )
    }
}

@Composable
private fun SelectedSentenceCard(
    sentence: String,
    translation: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    isDarkTheme: Boolean,
    onClose: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)
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
                    text = "Seçili Cümle",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kapat")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = sentence,
                style = TextStyle(
                    fontSize = fontSize,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkTheme) Color.White else Color.Black
                )
            )
            if (translation.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Çeviri",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = translation,
                    style = TextStyle(
                        fontSize = (fontSize.value * 0.9).sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal,
                        color = if (isDarkTheme) Color(0xFFB0BEC5) else Color(0xFF546E7A)
                    )
                )
            }
        }
    }
}
