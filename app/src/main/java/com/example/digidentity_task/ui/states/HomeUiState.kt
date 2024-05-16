package com.example.digidentity_task.ui.states

import androidx.compose.runtime.Immutable
import com.example.digidentity_task.model.CatalogItem
data class HomeUiState(
    val catalogUiState: CatalogUiState,
    val isRefreshing: Boolean
)

@Immutable
sealed interface CatalogUiState {
    data class Success(val items: List<CatalogItem>) : CatalogUiState
    data object Error : CatalogUiState
    data object Loading : CatalogUiState
}