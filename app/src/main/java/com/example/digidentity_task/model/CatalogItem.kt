package com.example.digidentity_task.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CatalogItem(
    val text: String,
    val confidence: Float,
    val image: String,
    @Json(name = "_id")
    val id: String,
)

fun CatalogItem.asEntity() = CatalogEntity(
    id = id,
    text = text,
    confidence = confidence,
    image = image
)