package com.readingapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.readingapp.data.database.dao.BookDao
import com.readingapp.data.database.dao.FlashcardDao
import com.readingapp.data.database.dao.ReadingStatsDao
import com.readingapp.data.database.dao.WordBookDao
import com.readingapp.data.database.entities.Book
import com.readingapp.data.database.entities.Flashcard
import com.readingapp.data.database.entities.ReadingStats
import com.readingapp.data.database.entities.WordBook

@Database(
    entities = [
        Book::class,
        WordBook::class,
        ReadingStats::class,
        Flashcard::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun wordBookDao(): WordBookDao
    abstract fun readingStatsDao(): ReadingStatsDao
    abstract fun flashcardDao(): FlashcardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reading_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
