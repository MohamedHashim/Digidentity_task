package com.example.digidentity_task.di

import android.content.Context
import androidx.room.Room
import com.example.digidentity_task.source.AppDatabase
import com.example.digidentity_task.source.CatalogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val APP_DATABASE_NAME = "catalog_database"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            APP_DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun providesCatalogDao(database: AppDatabase): CatalogDao = database.catalogDao()
}