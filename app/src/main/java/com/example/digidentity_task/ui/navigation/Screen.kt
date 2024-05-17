package com.example.digidentity_task.ui.navigation

sealed class Screen(val route: String) {
  data object CatalogList : Screen("catalog")
  data object CatalogItemDetails : Screen("catalog_item_details/{itemId}")
}