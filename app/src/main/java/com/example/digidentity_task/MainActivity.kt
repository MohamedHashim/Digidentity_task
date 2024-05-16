package com.example.digidentity_task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.digidentity_task.ui.navigation.UiStateCatalogNavHost
import com.example.digidentity_task.ui.screens.catalog_list.CatalogListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                androidx.compose.material.Scaffold { paddingValues ->
                    UiStateCatalogNavHost(
                        navController = navController,
                        modifier = Modifier
                            .padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogItemCardPreview() {
    MaterialTheme {
        CatalogListScreen(rememberNavController(), hiltViewModel())
    }
}