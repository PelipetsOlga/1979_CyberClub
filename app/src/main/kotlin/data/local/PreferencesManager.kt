package com.application.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val FIRST_LAUNCH_KEY = booleanPreferencesKey("is_first_launch")
    }

    val isFirstLaunch: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[FIRST_LAUNCH_KEY] ?: true
    }

    suspend fun isFirstLaunch(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[FIRST_LAUNCH_KEY] ?: true
        }.first()
    }

    suspend fun setFirstLaunchCompleted() {
        dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH_KEY] = false
        }
    }
}
