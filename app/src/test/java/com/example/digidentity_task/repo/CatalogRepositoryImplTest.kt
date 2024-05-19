package com.example.digidentity_task.repo

import android.util.Log
import com.example.digidentity_task.model.CatalogEntity
import com.example.digidentity_task.model.CatalogItem
import com.example.digidentity_task.model.PagedCatalogEntity
import com.example.digidentity_task.model.asEntity
import com.example.digidentity_task.model.asExternalModel
import com.example.digidentity_task.source.CatalogApi
import com.example.digidentity_task.source.CatalogDao
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert

@ExperimentalCoroutinesApi
class CatalogRepositoryImplTest {

    private lateinit var repository: CatalogRepositoryImpl
    private val catalogApi: CatalogApi = mock()
    private val catalogDao: CatalogDao = mock()

    @Before
    fun setUp() {
        repository = CatalogRepositoryImpl(catalogApi, catalogDao)
    }

    @Test
    fun `getRemoteCatalog emits items on success`() = runTest {
        val mockItems = listOf(CatalogItem("1", 1.0f, "url", "id"))
        whenever(catalogApi.getCatalogItems()).thenReturn(mockItems)

        val result = repository.getRemoteCatalog(0, "").first()

        assert(result == mockItems)
    }

    @Test
    fun `getRemoteCatalog emits empty list when API returns empty`() = runTest {
        whenever(catalogApi.getCatalogItems()).thenReturn(emptyList())

        val result = repository.getRemoteCatalog(1, "").first()

        assert(result.isEmpty())
    }

    @Test(expected = Exception::class)
    fun `getRemoteCatalog throws exception on API error`() = runTest {
        whenever(catalogApi.getCatalogItems()).thenThrow(RuntimeException())

        repository.getRemoteCatalog(0, "").first()
    }

    @Test
    fun `getCachedCatalog emits items on success`() = runTest {
        val pagedCatalogEntity =
            listOf(PagedCatalogEntity(1, listOf(CatalogEntity("1", 1.0f, "url", "id"))))

        whenever(catalogDao.getCatalogPagedItems()).thenReturn(flowOf(pagedCatalogEntity))

        val expected = pagedCatalogEntity.flatMap { it.items }.map { it.asExternalModel() }
        val result = repository.getCachedCatalog(1).first()

        assertEquals(expected.size, result.size)
        assertEquals(expected.first().text, result.first().text)
    }

    @Test
    fun `getCachedCatalog emits empty list when cache is empty`() = runTest {
        whenever(catalogDao.getCatalogPagedItems()).thenReturn(flowOf(emptyList()))

        val result = repository.getCachedCatalog(0).first()

        assert(result.isEmpty())
    }

    @Test(expected = Exception::class)
    fun `getCachedCatalog emits empty list when cache throws error exception`() = runTest {
        whenever(catalogDao.getCatalogPagedItems()).thenThrow(RuntimeException())

        repository.getCachedCatalog(0).first()
    }

    @Test
    fun `updateCachedCatalogItems updates items in cache`() = runTest {
        val pagedCatalogEntity =
            PagedCatalogEntity(0, listOf(CatalogItem("1", 1.0f, "url", "id").asEntity()))

        repository.updateCachedCatalogItems(pagedCatalogEntity)

        verify(catalogDao).savePagedCatalogItems(pagedCatalogEntity)
    }

    @Test
    fun `getCatalogRefreshTrigger emits Unit on refresh`() = runTest {
        val triggerFlow = repository.getCatalogRefreshTrigger().first()

        assert(triggerFlow == Unit)
    }
}