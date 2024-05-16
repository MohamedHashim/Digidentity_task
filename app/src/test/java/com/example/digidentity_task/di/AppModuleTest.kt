package com.example.digidentity_task.di

import com.example.digidentity_task.model.CatalogJsonConverter
import com.example.digidentity_task.source.CatalogApi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AppModuleTest {

    private lateinit var appModule: AppModule

    @Before
    fun setUp() {
        appModule = AppModule()
    }
    @Test
    fun `provideApi returns CatalogApi instance`() {
        val okHttpClient = appModule.provideOkHttpClient()
        val moshi = appModule.provideMoshi()
        val api = appModule.provideApi(okHttpClient, moshi)
        assertTrue(api is CatalogApi)
    }
    @Test
    fun `OkHttpClient includes loggingInterceptor`() {
        val client = appModule.provideOkHttpClient()
        assertTrue(client.interceptors.any { it is HttpLoggingInterceptor && it.level == HttpLoggingInterceptor.Level.BODY })
    }

    @Test
    fun `Moshi is correctly configured with CatalogJsonConverter and KotlinJsonAdapterFactory`() {
        val moshi = appModule.provideMoshi()
        assertNotNull(moshi.adapter(CatalogJsonConverter::class.java))
        assertNotNull(moshi.adapter(KotlinJsonAdapterFactory::class.java))
    }
}