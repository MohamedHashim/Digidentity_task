package com.example.digidentity_task.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catalog")
data class CatalogEntity(
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "confidence") val confidence: Float,
    @ColumnInfo(name = "image") val image: String,
    @PrimaryKey val id: String,
)
fun CatalogEntity.asExternalModel() = CatalogItem(
    id = id,
    text = text,
    confidence = confidence,
    image = image
)