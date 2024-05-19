package com.example.digidentity_task.repo

import android.util.Log
import com.example.digidentity_task.model.CatalogItem
import com.example.digidentity_task.model.CatalogEntity
import com.example.digidentity_task.model.PagedCatalogEntity
import com.example.digidentity_task.source.CatalogApi
import com.example.digidentity_task.source.CatalogDao
import com.example.digidentity_task.model.asEntity
import com.example.digidentity_task.model.asExternalModel
import com.example.digidentity_task.ui.states.CatalogUiState
import com.example.digidentity_task.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.jvm.Throws

class CatalogRepositoryImpl @Inject constructor(
    private val catalogApi: CatalogApi,
    private val catalogDao: CatalogDao
) : CatalogRepository {

    private val refreshTrigger = MutableStateFlow(Unit)

    override fun getPagedCatalog(page: Int, maxId: String): Flow<List<CatalogItem>> {
        return catalogDao.getCatalogPagedItems()
            .map { pageCatalogEntityList ->
                pageCatalogEntityList.flatMap { it.items }.map(CatalogEntity::asExternalModel)
            }.flatMapConcat { catalogItemList ->
                if (catalogItemList.isEmpty()) {
                    getRemoteCatalog(page, maxId)
                } else {
                    flow { emit(catalogItemList) }
                }
            }.catch { e ->
                Log.e("getPagedCatalog", "Error fetching catalog list: ${e.message}.")
                throw e
            }
    }


    override fun getCachedCatalog(page: Int): Flow<List<CatalogItem>> {
        return catalogDao.getCatalogPagedItems().map { pagedCatalogEntityList ->
            pagedCatalogEntityList.firstOrNull()?.items?.map(CatalogEntity::asExternalModel)
                ?: listOf()
        }.catch { e ->
            Log.d("getCachedCatalog", "Error fetching cached catalog list: ${e.message}.")
        }
    }

    override suspend fun getRemoteCatalog(
        page: Int,
        maxId: String
    ): Flow<List<CatalogItem>> =
        flow {
            try {
                val remoteCatalogItems = if (maxId.isEmpty()) {
                    catalogApi.getCatalogItems() ?: listOf()
                } else {
                    catalogApi.getCatalogPagedItems(maxId) ?: listOf()
                }
                if (remoteCatalogItems.isNotEmpty()) {
                    updateCachedCatalogItems(
                        PagedCatalogEntity(
                            page,
                            remoteCatalogItems.map(CatalogItem::asEntity)
                        )
                    )
                }
                emit(remoteCatalogItems)
                refreshTrigger.emit(Unit)
            } catch (e: Exception) {
                Log.d("getRemoteCatalog", "Error fetching remote catalog list: ${e.message}.")
                throw e
            }
        }

    suspend fun updateCachedCatalogItems(pagedCatalogEntity: PagedCatalogEntity) {
        catalogDao.savePagedCatalogItems(pagedCatalogEntity)
    }

    fun getCatalogRefreshTrigger(): Flow<Unit> = refreshTrigger

}