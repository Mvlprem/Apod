package com.mvlprem.apod.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Name of the DataStore
 */
private const val NAME = "themes"

/**
 * Provides DataStore
 * @param context is required to create DataStore
 */
class DataStore(context: Context) {

    /**
     * Provides name of the key & value type is Int.
     */
    private object PreferenceKey {
        val name = intPreferencesKey("user_theme")
    }

    /**
     * Creating DataStore using context
     */
    private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(NAME)
    private val dataStore: DataStore<Preferences> = context._dataStore

    /**
     * Suspend function should be called only from coroutine
     * Takes in an Int arg and saves in DataStore by assigning it to key
     * @param value of user selected theme
     */
    suspend fun save(value: Int) {
        dataStore.edit {
            it[PreferenceKey.name] = value
        }
    }

    /**
     * Calling this returns the value stored in DataStore
     * for the key {@link PreferenceKey}
     * The return type is Flow<Int> so it's need to be collected or
     * Observe as LiveData
     * @return 2 if empty
     */
    val read: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            val int: Int = it[PreferenceKey.name] ?: 2
            int
        }
}