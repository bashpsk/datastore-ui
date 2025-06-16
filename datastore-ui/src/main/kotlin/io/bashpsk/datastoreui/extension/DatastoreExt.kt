package io.bashpsk.datastoreui.extension

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> DataStore<Preferences>.getPreference(key: Preferences.Key<T>, initial: T): Flow<T> {

    return this.data.mapLatest { preferences ->

        preferences[key] ?: initial
    }.flowOn(context = Dispatchers.IO)
}

@Throws(IOException::class, Exception::class)
suspend fun <T> DataStore<Preferences>.setPreference(key: Preferences.Key<T>, value: T) {

    updateData { preferences -> preferences.toMutablePreferences().apply { this[key] = value } }
}