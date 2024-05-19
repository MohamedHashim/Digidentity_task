package com.example.digidentity_task.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.digidentity_task.model.PagedCatalogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogDao {
    @Query("SELECT * FROM pagedCatalog")
    fun getCatalogPagedItems(): Flow<List<PagedCatalogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePagedCatalogItems(pagedCatalog: PagedCatalogEntity)
}