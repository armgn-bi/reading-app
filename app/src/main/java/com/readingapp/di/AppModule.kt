package com.readingapp.di

import com.readingapp.data.database.AppDatabase
import com.readingapp.data.database.dao.BookDao
import com.readingapp.data.repository.BookRepository
import com.readingapp.viewmodel.BookListViewModel
import com.readingapp.viewmodel.ReaderViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().bookDao() }
    single { BookRepository(get()) }

    viewModel { BookListViewModel(get()) }
    viewModel { (bookId: Long) -> ReaderViewModel(get(), bookId) }
}
