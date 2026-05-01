package com.readingapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.readingapp.data.database.entities.Flashcard
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards WHERE nextReviewAt <= :currentTime ORDER BY nextReviewAt")
    fun getDueFlashcards(currentTime: Long): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE wordId = :wordId")
    fun getFlashcardByWordId(wordId: Long): Flow<Flashcard?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: Flashcard): Long

    @Update
    suspend fun updateFlashcard(flashcard: Flashcard)

    @Delete
    suspend fun deleteFlashcard(flashcard: Flashcard)

    @Query("UPDATE flashcards SET box = :box, lastReviewedAt = :lastReviewedAt, nextReviewAt = :nextReviewAt WHERE id = :id")
    suspend fun updateFlashcardReview(
        id: Long,
        box: Int,
        lastReviewedAt: Long = System.currentTimeMillis(),
        nextReviewAt: Long
    )
}
