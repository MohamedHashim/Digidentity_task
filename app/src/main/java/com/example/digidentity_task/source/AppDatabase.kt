package com.example.digidentity_task.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.digidentity_task.model.CatalogEntity
import com.example.digidentity_task.model.PagedCatalogEntity
import com.example.digidentity_task.utils.Converters

private const val DATABASE_VERSION = 1

@Database(
    entities = [CatalogEntity::class, PagedCatalogEntity::class],
    version = DATABASE_VERSION,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catalogDao(): CatalogDao
}