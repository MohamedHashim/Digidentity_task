package com.example.digidentity_task.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.digidentity_task.model.CatalogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogDao {

    @Query("SELECT * FROM catalog")
    fun getCatalogItems(): Flow<List<CatalogEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveCatalogItems(catalogItems: List<CatalogEntity>)

    @Query(value = "DELETE FROM catalog")
    suspend fun deleteCatalog()

    @Transaction
    suspend fun deleteAndInsert(items: List<CatalogEntity>) {
        deleteCatalog()
        saveCatalogItems(items)
    }
}