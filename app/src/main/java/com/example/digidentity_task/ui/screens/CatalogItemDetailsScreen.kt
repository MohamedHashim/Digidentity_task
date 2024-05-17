package com.example.digidentity_task.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.digidentity_task.ui.components.AppBar
import com.example.digidentity_task.ui.components.CatalogItemImage
import com.example.digidentity_task.ui.screens.catalog_list.CatalogListViewModel


@Composable
fun CatalogItemDetailsScreen(
    itemId: String,
    navController: NavHostController?,
    viewModel: CatalogListViewModel = hiltViewModel()
) {
    val catalogItem = viewModel.getCatalogItemById(itemId)
    Scaffold(topBar = {
        AppBar(
            title = "Catalog item details",
            icon = Icons.Default.ArrowBack
        ) {
            navController?.navigateUp()
        }
    }) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                catalogItem?.image?.let { CatalogItemImage(it, 240.dp) }
                catalogItem?.text?.let {
                    Text(
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        text = "Text: $it",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                catalogItem?.confidence?.let {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = "Confidence: $it",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                catalogItem?.id?.let {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = "ID: $it",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogItemDetailsScreenPreview() {
    CatalogItemDetailsScreen(itemId = "661ce5fc7e7e4", null)
}
