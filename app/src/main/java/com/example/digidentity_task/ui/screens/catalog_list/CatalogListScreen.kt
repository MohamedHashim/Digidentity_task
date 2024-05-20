package com.example.digidentity_task.ui.screens.catalog_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.digidentity_task.ui.states.ListState

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
            is CatalogUiState.Success -> CatalogList(catalogUiState.items, navController, viewModel)
            is CatalogUiState.Error -> ErrorPlaceholder(viewModel)
            is CatalogUiState.Loading -> LoadingIndicator()
        }

        PullRefreshIndicator(
            uiState.isRefreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )

    }
}

@Composable
fun CatalogList(
    items: List<CatalogItem>,
    navController: NavController,
    viewModel: CatalogListViewModel
) {
    val lazyColumnListState = rememberLazyListState()
    val shouldStartPaginate = remember {
        derivedStateOf {
            viewModel.canPaginate && (lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -9) >= (lazyColumnListState.layoutInfo.totalItemsCount - 3)
        }
    }

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value && viewModel.listState != ListState.PAGINATION_EXHAUST) {
            viewModel.loadMoreCatalogItems(items.last().id)

        }
    }

    Scaffold(topBar = {
        AppBar(
            title = "Catalog",
            icon = Icons.Default.Home
        ) {}
    }) { paddingValues ->

        LazyColumn(
            state = lazyColumnListState,
            modifier = Modifier
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
            item(
                key = viewModel.listState,
            ) {
                when (viewModel.listState) {

                    ListState.PAGINATING -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(text = "Pagination Loading")

                            androidx.compose.material.CircularProgressIndicator(color = Color.Black)
                        }
                    }

                    ListState.PAGINATION_EXHAUST -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp, vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Icon(imageVector = Icons.Rounded.Face, contentDescription = "")

                            Text(text = "Nothing left")
                        }
                    }

                    ListState.ERROR -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = "",
                                Modifier.padding(end = 8.dp)
                            )
                            Text(text = "Sorry! We can't load more items")
                        }
                    }

                    else -> {}
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