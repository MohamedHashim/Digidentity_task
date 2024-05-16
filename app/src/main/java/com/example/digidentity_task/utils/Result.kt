package com.example.digidentity_task.utils

import android.util.Log
import java.io.IOException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen

private const val RETRY_TIME_IN_MILLIS = 60_000L

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    data object Loading : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Log.d("CatalogListScreen", "YES YES+${it.toString()} ")

            Result.Success(it)
        }
        .onStart { emit(Result.Loading)
            Log.d("CatalogListScreen", "YES YES")
        }
        .retryWhen { cause, attempt ->
            if (attempt < 1 && cause is IOException) {
                emit(Result.Error(cause))

                delay(RETRY_TIME_IN_MILLIS)
                true
            } else {
                false
            }
        }
        .catch { emit(Result.Error(it)) }
}
