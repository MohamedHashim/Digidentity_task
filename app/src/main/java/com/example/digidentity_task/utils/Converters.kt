package com.example.digidentity_task.utils

import androidx.room.TypeConverter
import com.example.digidentity_task.model.CatalogEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val listType = Types.newParameterizedType(List::class.java, CatalogEntity::class.java)
    private val jsonAdapter = moshi.adapter<List<CatalogEntity>>(listType)

    @TypeConverter
    fun fromCatalogEntityList(value: List<CatalogEntity>?): String {
        return jsonAdapter.toJson(value)
    }

    @TypeConverter
    fun toCatalogEntityList(value: String): List<CatalogEntity>? {
        return jsonAdapter.fromJson(value)
    }
}