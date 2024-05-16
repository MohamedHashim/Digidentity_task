package com.example.digidentity_task.repo

import com.example.digidentity_task.model.CatalogEntity
import com.example.digidentity_task.model.CatalogItem
import com.example.digidentity_task.source.CatalogApi
import com.example.digidentity_task.source.CatalogDao
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class CatalogRepositoryImplTest {

    private lateinit var repository: CatalogRepositoryImpl
    private val catalogApi: CatalogApi = mock()
    private val catalogDao: CatalogDao = mock()

    @Before
    fun setUp() {
        repository = CatalogRepositoryImpl(catalogApi, catalogDao)
    }
    @Test
    fun `getCatalog triggers refreshCatalog when local database is empty`() = runTest {
        whenever(catalogDao.getCatalogItems()).thenReturn(flowOf(emptyList()))

        repository.getCatalog().collect{}

        verify(catalogApi, times(1)).getCatalogItems()
    }

    @Test
    fun `getCatalog does not trigger refreshCatalog when local database is not empty`() = runTest {
        whenever(catalogDao.getCatalogItems()).thenReturn(flowOf(listOf(CatalogEntity("1", 12.0f, "image.png", "12435"))))

        repository.getCatalog().collect{}

        verify(catalogApi, times(0)).getCatalogItems()
    }

    @Test
    fun `refreshCatalog clears and inserts items on non-empty API response`() = runTest {
        val apiItems = listOf(CatalogItem("Text", 0.99f, "image", "1"))
        whenever(catalogApi.getCatalogItems()).thenReturn(apiItems)

        repository.refreshCatalog()

        verify(catalogDao, times(1)).deleteAndInsert(any())
    }

    @Test
    fun `refreshCatalog does not modify local database on empty API response`() = runTest {
        whenever(catalogApi.getCatalogItems()).thenReturn(emptyList())

        repository.refreshCatalog()

        verify(catalogDao, times(0)).deleteAndInsert(any())
    }

    @Test
    fun `refreshCatalog triggers refreshTrigger emission`() = runTest {
        val apiItems = listOf(CatalogItem("Text", 0.99f, "image", "1"))
        whenever(catalogApi.getCatalogItems()).thenReturn(apiItems)

        val emissions = repository.getCatalogRefreshTrigger().take(1).toList()

        repository.refreshCatalog()

        assertTrue(emissions.isNotEmpty())
    }

    @Test
    fun `getCatalog does not modify local database on API failure`() = runTest {
        whenever(catalogApi.getCatalogItems()).thenThrow(RuntimeException("API failure"))
        whenever(catalogDao.getCatalogItems()).thenReturn(flowOf(listOf(CatalogEntity("1", 12.0f, "image.png", "12435"))))

        repository.getCatalog().collect{}

        verify(catalogDao, never()).deleteAndInsert(any())
    }

}