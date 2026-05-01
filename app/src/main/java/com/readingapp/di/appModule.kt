package com.readingapp.di

import android.content.Context
import com.readingapp.data.database.AppDatabase
import com.readingapp.data.database.dao.BookDao
import com.readingapp.data.database.dao.FlashcardDao
import com.readingapp.data.database.dao.ReadingStatsDao
import com.readingapp.data.database.dao.WordBookDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    // Database
    single { AppDatabase.getDatabase(androidContext()) }

    // DAOs
    single { get<AppDatabase>().bookDao() }
    single { get<AppDatabase>().wordBookDao() }
    single { get<AppDatabase>().readingStatsDao() }
    single { get<AppDatabase>().flashcardDao() }
}
