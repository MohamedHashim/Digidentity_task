package com.example.digidentity_task.ui.screens.catalog_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digidentity_task.model.CatalogItem
import com.example.digidentity_task.repo.CatalogRepositoryImpl
import com.example.digidentity_task.ui.states.CatalogUiState
import com.example.digidentity_task.ui.states.HomeUiState
import com.example.digidentity_task.utils.WhileUiSubscribed
import com.example.digidentity_task.utils.Result
import com.example.digidentity_task.utils.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogListViewModel @Inject constructor(
    private val catalogRepository: CatalogRepositoryImpl
) : ViewModel() {

    private val _catalogItems = MutableStateFlow<Result<List<CatalogItem>>>(Result.Loading)
    private val catalogItems: StateFlow<Result<List<CatalogItem>>> = _catalogItems.asStateFlow()

    private val isRefreshing = MutableStateFlow(false)


    val uiState: StateFlow<HomeUiState> = combine(
        catalogItems,
        isRefreshing,
    ) { catalogItems, refreshing ->

        val catalogUiState: CatalogUiState = when (catalogItems) {
            is Result.Success -> CatalogUiState.Success(catalogItems.data)
            is Result.Loading -> CatalogUiState.Loading
            is Result.Error -> CatalogUiState.Error
        }

        HomeUiState(
            catalogUiState,
            refreshing
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = HomeUiState(
            CatalogUiState.Loading,
            isRefreshing = false
        )
    )
    private val exceptionHandler = CoroutineExceptionHandler { context, exception ->
        viewModelScope.launch {
            Log.d("CatalogListScreen", "exceptionHandler")
            _catalogItems.emit(Result.Error(exception))
        }
    }

    init {
        observeCatalogUpdates()
    }

    private fun observeCatalogUpdates() {
        viewModelScope.launch(exceptionHandler) {
            catalogRepository.getCatalogRefreshTrigger().collect {
                loadCatalogItems()
            }
        }
    }

    private fun loadCatalogItems() {
        viewModelScope.launch(exceptionHandler) {
            catalogRepository.getPagedCatalog().asResult().collect { result ->
                _catalogItems.emit(result)
            }
        }
    }

     fun loadMoreCatalogItems(page: Int, maxId: String) {
        viewModelScope.launch(exceptionHandler) {
            catalogRepository.getRemoteCatalog(page, maxId).asResult().collect { result ->
                _catalogItems.emit(result)
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch(exceptionHandler) {
            isRefreshing.emit(true)
            catalogRepository.getRemoteCatalog().asResult().collect { result ->
                if(result is Result.Success)
                _catalogItems.emit(result)
                else
                    isRefreshing.emit(false)
            }
        }
    }

    fun getCatalogItemById(itemId: String): CatalogItem? {
        val catalogItemsList: List<CatalogItem> = when (val result = _catalogItems.value) {
            is Result.Success -> result.data
            else -> emptyList()
        }
        return catalogItemsList.find { it.id == itemId }
    }
}