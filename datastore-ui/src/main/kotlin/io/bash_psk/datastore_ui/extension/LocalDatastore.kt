package io.bash_psk.datastore_ui.extension

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

val LocalDatastore = staticCompositionLocalOf<DataStore<Preferences>> {
    error(message = "CompositionLocal LocalDatastore not present")
}