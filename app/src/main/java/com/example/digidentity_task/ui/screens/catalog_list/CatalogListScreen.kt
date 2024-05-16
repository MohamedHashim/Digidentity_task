package com.example.digidentity_task.ui.screens.catalog_list

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.digidentity_task.model.CatalogItem
import com.example.digidentity_task.ui.components.AppBar
import com.example.digidentity_task.ui.components.CatalogItemCard
import com.example.digidentity_task.ui.components.LoadingIndicator
import com.example.digidentity_task.ui.screens.ErrorPlaceholder
import com.example.digidentity_task.ui.states.CatalogUiState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CatalogListScreen(
    navController: NavController,
    viewModel: CatalogListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullRefreshState(refreshing = uiState.isRefreshing, onRefresh = {
        viewModel.onRefresh()
    })

    Box(Modifier.pullRefresh(pullRefreshState)) {

        when (val catalogUiState = uiState.catalogUiState) {
            is CatalogUiState.Success -> CatalogList(catalogUiState.items, navController)
            is CatalogUiState.Error -> ErrorPlaceholder(viewModel)
            is CatalogUiState.Loading -> LoadingIndicator()
        }

        PullRefreshIndicator(
            uiState.isRefreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )

    }
    Log.d("CatalogListScreen", "uiState2: $uiState")
}


@Composable
fun CatalogList(
    items: List<CatalogItem>,
    navController: NavController,
) {
    Scaffold(topBar = {
        AppBar(
            title = "Catalog",
            icon = Icons.Default.Home
        ) {}
    }) { paddingValues ->

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp
            )
        ) {
            items(items) { catalogItem ->
                CatalogItemCard(catalogItem = catalogItem) {
                    navController.navigate("catalog_item_details/${catalogItem.id}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogListScreenPreview() {
    CatalogListScreen(rememberNavController(), hiltViewModel())
}