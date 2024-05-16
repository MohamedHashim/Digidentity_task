package com.example.digidentity_task.utils

import kotlinx.coroutines.flow.SharingStarted


private const val StopTimeoutMillis: Long = 5000

val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)