package com.application.data.repository

import com.application.data.local.PreferencesManager
import com.application.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : AppRepository {
    
    override val isFirstLaunch: Flow<Boolean> = preferencesManager.isFirstLaunch
    
    override suspend fun isFirstLaunch(): Boolean {
        return preferencesManager.isFirstLaunch()
    }
    
    override suspend fun setFirstLaunchCompleted() {
        preferencesManager.setFirstLaunchCompleted()
    }
}


