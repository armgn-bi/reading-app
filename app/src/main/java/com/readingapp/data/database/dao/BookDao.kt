package com.readingapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.readingapp.data.database.entities.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY lastReadAt DESC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :id")
    fun getBookById(id: Long): Flow<Book?>

    @Query("SELECT * FROM books WHERE filePath = :filePath")
    suspend fun getBookByFilePath(filePath: String): Book?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("UPDATE books SET lastReadAt = :timestamp WHERE id = :id")
    suspend fun updateLastReadAt(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE books SET currentPage = :page, currentPosition = :position WHERE id = :id")
    suspend fun updateReadingProgress(id: Long, page: Int, position: Int)
}
