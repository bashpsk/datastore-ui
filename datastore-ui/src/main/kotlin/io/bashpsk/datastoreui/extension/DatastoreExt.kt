package io.bashpsk.datastoreui.extension

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import io.bashpsk.datastoreui.utils.LOG_TAG
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

internal val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

    Log.e(LOG_TAG, throwable.message, throwable)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> DataStore<Preferences>.getPreference(key: Preferences.Key<T>, initial: T): Flow<T> {

    return this.data.mapLatest { preferences ->

        preferences[key] ?: initial
    }.flowOn(context = Dispatchers.IO)
}

suspend fun <T> DataStore<Preferences>.setPreference(key: Preferences.Key<T>, value: T) {

    withContext(context = Dispatchers.IO + exceptionHandler) {

        updateData { preferences -> preferences.toMutablePreferences().apply { this[key] = value } }
    }
}

suspend fun <T> DataStore<Preferences>.resetPreference(key: Preferences.Key<T>) {

    withContext(context = Dispatchers.IO + exceptionHandler) {

        this@resetPreference.edit { preferences -> preferences.remove(key = key) }
    }
}