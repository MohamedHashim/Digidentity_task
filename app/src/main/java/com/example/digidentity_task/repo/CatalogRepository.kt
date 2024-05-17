package com.example.digidentity_task.repo

import com.example.digidentity_task.model.CatalogItem
import kotlinx.coroutines.flow.Flow

interface CatalogRepository {

    fun getPagedCatalog(page: Int = 1, maxId: String = ""): Flow<List<CatalogItem>>

    suspend fun getRemoteCatalog(page: Int = 1, maxId: String = ""): Flow<List<CatalogItem>>

    fun getCachedCatalog(page: Int = 1): Flow<List<CatalogItem>>

}
