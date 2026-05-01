package com.readingapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.readingapp.data.database.entities.WordBook
import kotlinx.coroutines.flow.Flow

@Dao
interface WordBookDao {
    @Query("SELECT * FROM word_book ORDER BY createdAt DESC")
    fun getAllWords(): Flow<List<WordBook>>

    @Query("SELECT * FROM word_book WHERE id = :id")
    fun getWordById(id: Long): Flow<WordBook?>

    @Query("SELECT * FROM word_book WHERE word = :word LIMIT 1")
    suspend fun getWordByWord(word: String): WordBook?

    @Query("SELECT * FROM word_book WHERE word LIKE :query OR meaning LIKE :query ORDER BY createdAt DESC")
    fun searchWords(query: String): Flow<List<WordBook>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordBook): Long

    @Update
    suspend fun updateWord(word: WordBook)

    @Delete
    suspend fun deleteWord(word: WordBook)

    @Query("UPDATE word_book SET reviewCount = reviewCount + 1, nextReviewAt = :nextReviewAt WHERE id = :id")
    suspend fun incrementReviewCount(id: Long, nextReviewAt: Long)
}
