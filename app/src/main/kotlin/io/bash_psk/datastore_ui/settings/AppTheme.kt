package io.bash_psk.datastore_ui.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

enum class AppTheme {

    SYSTEM,
    DARK,
    LIGHT;

    companion object {

        @Composable
        fun getTheme(theme: String): Boolean {

            return when (valueOf(value = theme)) {

                SYSTEM -> isSystemInDarkTheme()
                DARK -> true
                LIGHT -> false
            }
        }
    }
}