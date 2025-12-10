package com.application.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    val isFirstLaunch: Flow<Boolean>
    suspend fun isFirstLaunch(): Boolean
    suspend fun setFirstLaunchCompleted()
}
