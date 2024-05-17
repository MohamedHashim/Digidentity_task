package com.example.digidentity_task.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.digidentity_task.ui.screens.CatalogItemDetailsScreen
import com.example.digidentity_task.ui.screens.catalog_list.CatalogListScreen


@Composable
fun UiStateCatalogNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.CatalogList.route,
        modifier = modifier
    ) {
        composable(Screen.CatalogList.route) {
            CatalogListScreen(navController)
        }
        composable(Screen.CatalogItemDetails.route, arguments = listOf(navArgument("itemId") {
            type = NavType.StringType
        })) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("itemId")
                ?.let { CatalogItemDetailsScreen(it, navController) }
        }
    }
}
