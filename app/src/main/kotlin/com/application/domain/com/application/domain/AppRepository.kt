package com.application.com.application.domain.com.application.domain

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    val isFirstLaunch: Flow<Boolean>
    suspend fun isFirstLaunch(): Boolean
    suspend fun setFirstLaunchCompleted()
}


