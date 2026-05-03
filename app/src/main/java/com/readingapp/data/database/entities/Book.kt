package com.readingapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val filePath: String,
    val fileType: String,
    val translationFilePath: String? = null,
    val totalPages: Int? = null,
    val currentPage: Int = 0,
    val currentPosition: Int = 0,
    val createdAt: Long,
    val lastReadAt: Long? = null,
    val category: String? = null
)
