package com.example.digidentity_task.di

import com.example.digidentity_task.repo.CatalogRepository
import com.example.digidentity_task.repo.CatalogRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsCatalogRepository(catalogRepository: CatalogRepositoryImpl): CatalogRepository
}