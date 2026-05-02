package com.readingapp.data.database.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val filePath: String,
    val fileType: String, // txt, pdf, epub
    val translationFilePath: String? = null,
    val totalPages: Int? = null,
    val currentPage: Int = 0,
    val currentPosition: Int = 0, // karakter pozisyonu
    val createdAt: Long = System.currentTimeMillis(),
    val lastReadAt: Long? = null,
    val coverImage: ByteArray? = null,
    val category: String? = null,
    val isUriPersisted: Boolean = false // URI kalıcı hale getirildi mi
)
