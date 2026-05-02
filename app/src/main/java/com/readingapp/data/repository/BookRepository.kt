package com.readingapp.data.repository

import com.readingapp.data.database.dao.BookDao
import com.readingapp.data.database.entities.Book
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {

    fun getAllBooks(): Flow<List<Book>> = bookDao.getAllBooks()

    fun getBookById(id: Long): Flow<Book?> = bookDao.getBookById(id)

    suspend fun getBookByFilePath(filePath: String): Book? = bookDao.getBookByFilePath(filePath)

    suspend fun insertBook(book: Book): Long = bookDao.insertBook(book)

    suspend fun updateBook(book: Book) = bookDao.updateBook(book)

    suspend fun deleteBook(book: Book) = bookDao.deleteBook(book)

    suspend fun updateLastReadAt(id: Long) = bookDao.updateLastReadAt(id)

    suspend fun updateReadingProgress(id: Long, page: Int, position: Int) =
        bookDao.updateReadingProgress(id, page, position)
}
