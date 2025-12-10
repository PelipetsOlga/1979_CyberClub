package com.application.data.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.application.data.local.PreferencesManager
import com.application.domain.models.domain.models.ChecklistData
import com.application.domain.models.domain.models.PlaceFilter
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
        return preferencesManager.isFirstLaunch().first()
    }
    
    override suspend fun setFirstLaunchCompleted() {
        preferencesManager.setFirstLaunchCompleted()
    }

}


