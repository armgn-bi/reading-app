package com.readingapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_book")
data class WordBook(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val word: String,
    val reading: String? = null, // hiragana okunuş
    val meaning: String,
    val romaji: String? = null,
    val jlptLevel: String? = null, // N1-N5
    val exampleSentence: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val reviewCount: Int = 0,
    val nextReviewAt: Long? = null
)
