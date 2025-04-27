package io.bashpsk.datastoreui.preference

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bashpsk.datastoreui.extension.LocalDatastore
import io.bashpsk.datastoreui.extension.getPreference
import io.bashpsk.datastoreui.extension.setPreference
import kotlinx.coroutines.launch

@Composable
fun SwitchMenuPreference(
    modifier: Modifier = Modifier,
    key: () -> Preferences.Key<Boolean>,
    initialValue: () -> Boolean = { false },
    title: () -> String,
    leadingContent: @Composable (() -> Unit) = {},
    colors: MenuItemColors = MenuDefaults.itemColors(),
    onMenuDismiss: () -> Unit
) {

    val dataStore = LocalDatastore.current
    val coroutineScope = rememberCoroutineScope()

    val getSwitchState by dataStore.getPreference(
        key = key(),
        initial = initialValue()
    ).collectAsStateWithLifecycle(initialValue = initialValue())

    DropdownMenuItem(
        modifier = modifier,
        colors = colors,
        text = {

            Text(
                text = title(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = leadingContent,
        trailingIcon = {

            Switch(
                checked = getSwitchState,
                thumbContent = {

                    Icon(
                        modifier = Modifier.size(size = SwitchDefaults.IconSize),
                        imageVector = when (getSwitchState) {

                            true -> Icons.Filled.Check
                            false -> Icons.Filled.Close
                        },
                        contentDescription = "Switch Thumb"
                    )
                },
                onCheckedChange = null
            )
        },
        onClick = {

            coroutineScope.launch {

                dataStore.setPreference(key = key(), value = getSwitchState.not())
            }

            onMenuDismiss()
        }
    )
}