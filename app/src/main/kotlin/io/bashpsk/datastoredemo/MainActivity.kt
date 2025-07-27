package io.bashpsk.datastoredemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bashpsk.datastoredemo.ui.theme.DatastoreUITheme
import io.bashpsk.datastoreui.extension.LocalDatastore
import io.bashpsk.datastoreui.extension.getPreference

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val getAppTheme by datastore.getPreference(
                key = stringPreferencesKey("SINGLE-OPTION-MENU-PREFERENCE"),
                initial = AppTheme.SYSTEM.name
            ).collectAsStateWithLifecycle(initialValue = AppTheme.SYSTEM.name)

            CompositionLocalProvider(LocalDatastore provides datastore) {

                DatastoreUITheme(darkTheme = AppTheme.getTheme(theme = getAppTheme)) {

                    SampleScreen()
//                    ColorPickerDemoScreen()
                }
            }
        }
    }
}