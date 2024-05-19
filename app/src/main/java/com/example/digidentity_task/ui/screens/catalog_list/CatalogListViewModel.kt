package com.example.digidentity_task.ui.screens.catalog_list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digidentity_task.model.CatalogItem
import com.example.digidentity_task.repo.CatalogRepositoryImpl
import com.example.digidentity_task.ui.states.CatalogUiState
import com.example.digidentity_task.ui.states.HomeUiState
import com.example.digidentity_task.ui.states.ListState
import com.example.digidentity_task.utils.WhileUiSubscribed
import com.example.digidentity_task.utils.Result
import com.example.digidentity_task.utils.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
    var currentPage = 1
    var canPaginate by mutableStateOf(true)
    var listState by mutableStateOf(ListState.IDLE)


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
            Log.d("CatalogListScreen", "exceptionHandler ${exception.message} ")
            _catalogItems.emit(Result.Error(exception))
        }
    }

    init {
        observeCatalogUpdates()
    }

    private fun observeCatalogUpdates() {
        viewModelScope.launch(exceptionHandler) {
            catalogRepository.getCatalogRefreshTrigger().catch {
                _catalogItems.emit(Result.Error(it))
            }.collect {
                loadCatalogItems()
            }
        }
    }

    private fun loadCatalogItems() {
        viewModelScope.launch(exceptionHandler) {
            catalogRepository.getPagedCatalog().asResult().catch {
                _catalogItems.emit(Result.Error(it))
            }.collect { result ->
                Log.d("CatalogListViewModel", "fetched catalog items $result")

                when (result) {
                    is Result.Success -> {
                        _catalogItems.emit(result)
                    }

                    is Result.Error -> {
                        _catalogItems.emit(Result.Error(result.exception))
                        Log.d(
                            "CatalogListViewModel",
                            "Error fetching catalog items: ${result.exception}"
                        )
                    }

                    Result.Loading -> {
                        _catalogItems.emit(Result.Loading)
                    }
                }
            }
        }
    }

    fun loadMoreCatalogItems(maxID: String) {
        listState = ListState.PAGINATING

        viewModelScope.launch(exceptionHandler) {
            val nextPage = currentPage + 1
            catalogRepository.getRemoteCatalog(nextPage, maxID).asResult().catch {
                _catalogItems.emit(Result.Error(it))
            }.collect { result ->
                Log.d("CatalogListViewModel", "loadMoreCatalogItems: $result")
                when (result) {
                    is Result.Success -> {
                        if (result.data.isNotEmpty()) {
                            currentPage++
                            canPaginate = true
                            val currentItems =
                                (_catalogItems.value as? Result.Success)?.data.orEmpty()
                            _catalogItems.emit(Result.Success(currentItems + result.data))
                        } else {
                            canPaginate = false
                            listState = ListState.PAGINATION_EXHAUST
                        }
                    }

                    is Result.Error -> {
                        Log.d(
                            "CatalogListViewModel",
                            "Error fetching catalog items: ${result.exception}"
                        )
                        listState = ListState.ERROR
                        canPaginate = false
                    }

                    is Result.Loading -> {
                        listState = ListState.PAGINATING
                    }
                }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch(exceptionHandler) {
            isRefreshing.emit(true)
            catalogRepository.getRemoteCatalog().asResult().collect { result ->
                if (result is Result.Success)
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

    override fun onCleared() {
        listState = ListState.IDLE
        canPaginate = false
        super.onCleared()
    }
}