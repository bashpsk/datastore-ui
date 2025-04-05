package io.bash_psk.datastore_ui.preference

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bash_psk.datastore_ui.extension.LocalDatastore
import io.bash_psk.datastore_ui.extension.getPreference
import io.bash_psk.datastore_ui.extension.setPreference

@Composable
fun SwitchMenuPreference(
    modifier: Modifier = Modifier,
    keyString: () -> String,
    initialValue: () -> Boolean = { false },
    title: () -> String,
    leadingContent: @Composable (() -> Unit) = {},
    colors: MenuItemColors = MenuDefaults.itemColors(),
    onMenuDismiss: () -> Unit
) {

    val dataStore = LocalDatastore.current

    val getSwitchState by dataStore.getPreference(
        keyString = keyString(),
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

            dataStore.setPreference(keyString = keyString(), value = getSwitchState.not())
            onMenuDismiss()
        }
    )
}