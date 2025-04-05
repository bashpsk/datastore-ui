package io.bash_psk.datastore_ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bash_psk.datastore_ui.datastore.dataStore
import io.bash_psk.datastore_ui.extension.LocalDatastore
import io.bash_psk.datastore_ui.extension.getPreference
import io.bash_psk.datastore_ui.settings.AppTheme
import io.bash_psk.datastore_ui.ui.screen.SampleScreen
import io.bash_psk.datastore_ui.ui.theme.DatastoreUITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val getAppTheme by dataStore.getPreference(
                keyString = "SINGLE-OPTION-MENU-PREFERENCE",
                initial = AppTheme.SYSTEM.name
            ).collectAsStateWithLifecycle(initialValue = AppTheme.SYSTEM.name)

            CompositionLocalProvider(LocalDatastore provides dataStore) {

                DatastoreUITheme(darkTheme = AppTheme.getTheme(theme = getAppTheme)) {

                    SampleScreen()
                }
            }
        }
    }
}