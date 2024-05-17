package com.example.digidentity_task.source

import com.example.digidentity_task.model.CatalogItem
import retrofit2.http.GET
import retrofit2.http.Query

interface CatalogApi {
    @GET("items")
    suspend fun getCatalogItems(): List<CatalogItem>

    @GET("items")
    suspend fun getCatalogPagedItems(@Query("max_id") max_id: String): List<CatalogItem>
}
