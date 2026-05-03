package com.readingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.readingapp.ui.screens.BookListScreen
import com.readingapp.ui.screens.ReaderScreen
import com.readingapp.ui.screens.SettingsScreen
import com.readingapp.ui.screens.WordBookScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "book_list"
    ) {
        composable("book_list") {
            BookListScreen(
                viewModel = koinViewModel(),
                onBookClick = { bookId ->
                    navController.navigate("reader/$bookId")
                },
                onWordBookClick = {
                    navController.navigate("word_book")
                },
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }

        composable(
            route = "reader/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getLong("bookId") ?: 0L
            ReaderScreen(
                viewModel = koinViewModel { parametersOf(bookId) },
                onBack = {
                    navController.popBackStack()
                },
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }

        composable("word_book") {
            WordBookScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
