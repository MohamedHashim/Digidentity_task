package com.example.digidentity_task.repo

import com.example.digidentity_task.model.CatalogItem
import com.example.digidentity_task.model.CatalogEntity
import com.example.digidentity_task.source.CatalogApi
import com.example.digidentity_task.source.CatalogDao
import com.example.digidentity_task.model.asEntity
import com.example.digidentity_task.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val catalogApi: CatalogApi,
    private val catalogDao: CatalogDao
) : CatalogRepository {

    private val refreshTrigger = MutableStateFlow(Unit)

    override fun getCatalog(): Flow<List<CatalogItem>> {
        return catalogDao.getCatalogItems().map { catalogEntity ->
            catalogEntity.map(CatalogEntity::asExternalModel)
        }.onEach { catalogItems ->
            if (catalogItems.isEmpty()) {
                refreshCatalog()
            }
        }
    }

    override suspend fun refreshCatalog() {
        val apiCatalogItems = catalogApi.getCatalogItems() ?: listOf()
        if (apiCatalogItems.isNotEmpty()) {
            catalogDao.deleteAndInsert(apiCatalogItems.map(CatalogItem::asEntity))
        }
        refreshTrigger.emit(Unit)
    }

    fun getCatalogRefreshTrigger(): Flow<Unit> = refreshTrigger

}