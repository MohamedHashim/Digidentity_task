package com.example.digidentity_task.repo

import com.example.digidentity_task.model.CatalogItem
import kotlinx.coroutines.flow.Flow

interface CatalogRepository {
    fun getCatalog(): Flow<List<CatalogItem>>
    suspend fun refreshCatalog()

}
