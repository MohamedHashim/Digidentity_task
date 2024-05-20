package com.example.digidentity_task.ui.screens.catalog_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.digidentity_task.model.CatalogItem
import com.example.digidentity_task.repo.CatalogRepositoryImpl
import com.example.digidentity_task.utils.Result
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CatalogListViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var catalogRepository: CatalogRepositoryImpl

    private lateinit var viewModel: CatalogListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = CatalogListViewModel(catalogRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCatalogItems success updates catalogItems to success state with data`() = runTest {
        val mockData = listOf(CatalogItem("1", 1.0f, "url", "id"))
        whenever(catalogRepository.getPagedCatalog(1, "")).thenReturn(flowOf(mockData))

        viewModel.loadCatalogItems()

        assert(viewModel.catalogItems.value is Result.Success && (viewModel.catalogItems.value as Result.Success).data == mockData)
    }

    @Test
    fun `loadCatalogItems error updates catalogItems to error state`() = runTest {
        val exception = Exception("Error")
        whenever(catalogRepository.getPagedCatalog(1, "")).thenReturn(flow { throw exception })

        viewModel.loadCatalogItems()

        assert(viewModel.catalogItems.value is Result.Error && (viewModel.catalogItems.value as Result.Error).exception == exception)
    }

    @Test
    fun `loadMoreCatalogItems success appends data and updates state`() = runTest {
        val initialData = listOf(CatalogItem("1", 1.0f, "url", "id"))
        val newData = listOf(CatalogItem("2", 2.0f, "url2", "id2"))
        whenever(catalogRepository.getPagedCatalog(1, "")).thenReturn(flowOf(initialData))
        whenever(catalogRepository.getRemoteCatalog(2, "id")).thenReturn(flowOf(newData))

        viewModel.loadCatalogItems()
        viewModel.loadMoreCatalogItems("id")

        assert(viewModel.catalogItems.value is Result.Success && (viewModel.catalogItems.value as Result.Success).data == initialData + newData)
        assert(viewModel.canPaginate)
    }

    @Test
    fun `loadMoreCatalogItems error updates state to error and disables pagination`() = runTest {
        val exception = Exception("Load more error")
        whenever(catalogRepository.getRemoteCatalog(2, "id")).thenReturn(flow { throw exception })

        viewModel.loadMoreCatalogItems("id")

        assert(viewModel.catalogItems.value is Result.Error)
        assert(!viewModel.canPaginate)
    }

    @Test
    fun `onRefresh success reloads catalog items`() = runTest {
        val refreshedData = listOf(CatalogItem("3", 3.0f, "url3", "id3"))
        whenever(catalogRepository.getRemoteCatalog(1, "")).thenReturn(flowOf(refreshedData))

        viewModel.onRefresh()

        assert(viewModel.catalogItems.value is Result.Success && (viewModel.catalogItems.value as Result.Success).data == refreshedData)
    }

    @Test
    fun `onRefresh error updates isRefreshing to false`() = runTest {
        val exception = Exception("Refresh error")
        whenever(catalogRepository.getRemoteCatalog(1, "")).thenReturn(flow { throw exception })

        viewModel.onRefresh()

        assertFalse(viewModel.isRefreshing.value)
    }

    @Test
    fun `getCatalogItemById returns correct item when exists`() = runTest {
        val mockData =
            listOf(CatalogItem("1", 1.0f, "url", "id"), CatalogItem("2", 2.0f, "url2", "id2"))
        whenever(catalogRepository.getPagedCatalog(1, "")).thenReturn(flowOf(mockData))

        viewModel.loadCatalogItems()

        val item = viewModel.getCatalogItemById("id")

        assert(item != null && item.id == "id")
    }

    @Test
    fun `getCatalogItemById returns null when item does not exist`() = runTest {
        val mockData = listOf(CatalogItem("1", 1.0f, "url", "id"))
        whenever(catalogRepository.getPagedCatalog(1, "")).thenReturn(flowOf(mockData))

        viewModel.loadCatalogItems()

        val item = viewModel.getCatalogItemById("non_existent")

        assert(item == null)
    }
}