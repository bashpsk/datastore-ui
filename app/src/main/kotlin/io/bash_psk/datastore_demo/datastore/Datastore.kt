package io.bash_psk.datastore_demo.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "DATASTORE-UI-PSK")