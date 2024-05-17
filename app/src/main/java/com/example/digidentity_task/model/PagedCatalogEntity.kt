package com.example.digidentity_task.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pagedCatalog")
class PagedCatalogEntity(
    @PrimaryKey val page: Int,
    val items: List<CatalogEntity>
)

fun PagedCatalogEntity.asExternalModel() =
    items.map {
        it.asExternalModel()
    }
