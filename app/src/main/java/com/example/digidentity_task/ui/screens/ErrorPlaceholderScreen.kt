package com.example.digidentity_task.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.digidentity_task.R
import com.example.digidentity_task.ui.components.AppBar
import com.example.digidentity_task.ui.screens.catalog_list.CatalogListViewModel

@Composable
fun ErrorPlaceholder(viewModel: CatalogListViewModel) {
    ErrorPlaceholder(
        image = R.drawable.ic_error,
        message = "Sorry! Couldn't load catalog\n\n Please Check your internet connection\nand retry again.",
    ) {
        viewModel.onRefresh()
    }
}


@Composable
fun ErrorPlaceholder(
    image: Int,
    message: String,
    onRetryClick: () -> Unit
) {
    val painter = rememberAsyncImagePainter(image)
    Scaffold(topBar = {
        AppBar(
            title = "Catalog",
            icon = Icons.Default.Home
        ) {}
    }) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painter,
                    contentDescription = "Error image",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onRetryClick,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Retry")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPlaceholderPreview() {
    MaterialTheme {
        ErrorPlaceholder(
            image = R.drawable.ic_error,
            message = "Error: Failed to load catalog"
        ) {}
    }
}