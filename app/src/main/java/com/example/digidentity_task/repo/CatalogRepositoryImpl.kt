package com.example.digidentity_task.repo

import com.example.digidentity_task.model.CatalogItem
import com.example.digidentity_task.model.CatalogEntity
import com.example.digidentity_task.model.PagedCatalogEntity
import com.example.digidentity_task.source.CatalogApi
import com.example.digidentity_task.source.CatalogDao
import com.example.digidentity_task.model.asEntity
import com.example.digidentity_task.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val catalogApi: CatalogApi,
    private val catalogDao: CatalogDao
) : CatalogRepository {

    private val refreshTrigger = MutableStateFlow(Unit)
    override fun getPagedCatalog(page: Int, maxId: String): Flow<List<CatalogItem>> {
        return getCachedCatalog(page).onEach { catalogItems ->
            if (catalogItems.isEmpty()) {
                getRemoteCatalog(page, maxId)
            }
        }
    }

    override fun getCachedCatalog(page: Int): Flow<List<CatalogItem>> {
        return catalogDao.getCatalogPagedItems(page).map { catalogEntity ->
            catalogEntity.items.map(CatalogEntity::asExternalModel)
        }
    }

    override suspend fun getRemoteCatalog(page: Int, maxId: String): Flow<List<CatalogItem>> =
        flow {
            val remoteCatalogItems = if (maxId.isEmpty()) {
                catalogApi.getCatalogItems() ?: listOf()
            } else {
                catalogApi.getCatalogPagedItems(maxId) ?: listOf()
            }
            if (remoteCatalogItems.isNotEmpty())
                updateCachedCatalogItems(
                    PagedCatalogEntity(
                        page,
                        remoteCatalogItems.map(CatalogItem::asEntity)
                    )
                )
            refreshTrigger.emit(Unit)
            emit(remoteCatalogItems)
        }

    suspend fun updateCachedCatalogItems(pagedCatalogEntity: PagedCatalogEntity) {
        catalogDao.savePagedCatalogItems(pagedCatalogEntity)
    }

    fun getCatalogRefreshTrigger(): Flow<Unit> = refreshTrigger

}