package com.paulo.loginapplication.util

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.paulo.loginapplication.model.domain.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class DatastoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun addToDatastore(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit { settings ->
            settings[key] = value
        }
    }

    suspend fun removeFromDatastore(key: Preferences.Key<String>) {
        context.dataStore.edit { settings ->
            settings.remove(key)
        }
    }

    fun observeKeyValue(key: Preferences.Key<String>): Flow<String?> {
        return context.dataStore.data.map { settings ->
            settings[key]
        }
    }
}