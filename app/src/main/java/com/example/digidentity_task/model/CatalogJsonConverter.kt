package com.example.digidentity_task.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

data class CatalogResults(
    val results: List<CatalogItem>
)

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class WrapperCatalogResults

class CatalogJsonConverter {
    @WrapperCatalogResults
    @FromJson
    fun fromJson(json: CatalogResults): List<CatalogItem> {
        return json.results
    }

    @ToJson
    fun toJson(@WrapperCatalogResults value: List<CatalogItem>): CatalogResults {
        throw UnsupportedOperationException()
    }
}