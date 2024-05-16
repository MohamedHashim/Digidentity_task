package com.example.digidentity_task.source

import com.example.digidentity_task.model.CatalogItem
import retrofit2.http.GET

interface CatalogApi {
    @GET("items")
    suspend fun getCatalogItems(): List<CatalogItem>
}
