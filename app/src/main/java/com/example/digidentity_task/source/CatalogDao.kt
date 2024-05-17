package com.example.digidentity_task.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.digidentity_task.model.CatalogEntity
import com.example.digidentity_task.model.PagedCatalogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogDao {

    @Query("SELECT * FROM pagedCatalog WHERE page = :pageNumber")
    fun getCatalogPagedItems(pageNumber: Int): Flow<PagedCatalogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePagedCatalogItems(pagedCatalog: PagedCatalogEntity)
}