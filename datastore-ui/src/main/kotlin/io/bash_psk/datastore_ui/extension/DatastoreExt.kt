package io.bash_psk.datastore_ui.extension

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import io.bash_psk.datastore_ui.utils.SetLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

fun <T> DataStore<Preferences>.getPreference(keyString: String, initial: T): Flow<T> {

    val LOG_TAG = "DATASTORE-UI"

    return this.data.catch { throwable ->

        when (throwable) {

            is IOException -> {

                emit(value = emptyPreferences())
                SetLog.error(tag = LOG_TAG, throwable = throwable)
            }

            else -> SetLog.error(tag = LOG_TAG, throwable = throwable)
        }
    }.map { preferences ->

        findPreferenceKey(keyString = keyString, value = initial)?.let { key ->

            preferences[key] ?: initial
        } ?: initial
    }
}

fun <T> DataStore<Preferences>.setPreference(keyString: String, value: T) {

    val LOG_TAG = "DATASTORE-UI"

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->

        SetLog.error(tag = LOG_TAG, throwable = throwable)
    }

    CoroutineScope(context = SupervisorJob() + Dispatchers.IO).launch(context = exceptionHandler) {

        findPreferenceKey(keyString = keyString, value = value)?.let { key ->

            this@setPreference.edit { preferences -> preferences[key] = value }
        } ?: SetLog.error(message = "Invalid Preference Type!")
    }
}

private fun <T> findPreferenceKey(keyString: String, value: T): Preferences.Key<T>? {

    return when (value) {

        is Boolean -> booleanPreferencesKey(name = keyString)
        is ByteArray -> byteArrayPreferencesKey(name = keyString)
        is Double -> doublePreferencesKey(name = keyString)
        is Float -> floatPreferencesKey(name = keyString)
        is Int -> intPreferencesKey(name = keyString)
        is Long -> longPreferencesKey(name = keyString)
        is Set<*> -> stringSetPreferencesKey(name = keyString)
        is String -> stringPreferencesKey(name = keyString)
        else -> null
    } as Preferences.Key<T>
}