package com.example.digidentity_task.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.digidentity_task.model.CatalogEntity

private const val DATABASE_VERSION = 1

@Database(
    entities = [CatalogEntity::class],
    version = DATABASE_VERSION,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catalogDao(): CatalogDao
}