package com.example.digidentity_task.di

import com.example.digidentity_task.BuildConfig
import com.example.digidentity_task.model.CatalogJsonConverter
import com.example.digidentity_task.source.CatalogApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://marlove.net/e/mock/v1/"

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val authQueryAppenderInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            val url = chain.request().url
            val urlBuilder = url.newBuilder()
            chain.proceed(
                requestBuilder.header("Authorization", BuildConfig.API_KEY)
                    .url(urlBuilder.build())
                    .build()
            )
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authQueryAppenderInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(CatalogJsonConverter())
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    fun provideApi(okHttpClient: OkHttpClient, moshi: Moshi): CatalogApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(CatalogApi::class.java)
    }
}