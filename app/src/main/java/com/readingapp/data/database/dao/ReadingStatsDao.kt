package com.readingapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.readingapp.data.database.entities.ReadingStats
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingStatsDao {
    @Query("SELECT * FROM reading_stats WHERE bookId = :bookId ORDER BY date DESC")
    fun getStatsByBookId(bookId: Long): Flow<List<ReadingStats>>

    @Query("SELECT * FROM reading_stats WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getStatsByDateRange(startDate: Long, endDate: Long): Flow<List<ReadingStats>>

    @Query("SELECT SUM(durationSeconds) FROM reading_stats WHERE bookId = :bookId")
    suspend fun getTotalReadingTime(bookId: Long): Long?

    @Query("SELECT SUM(wordsRead) FROM reading_stats WHERE bookId = :bookId")
    suspend fun getTotalWordsRead(bookId: Long): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: ReadingStats)
}
