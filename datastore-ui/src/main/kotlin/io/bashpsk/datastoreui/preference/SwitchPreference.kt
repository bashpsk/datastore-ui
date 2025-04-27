package io.bashpsk.datastoreui.preference

import androidx.annotation.FloatRange
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bashpsk.datastoreui.extension.LocalDatastore
import io.bashpsk.datastoreui.extension.getPreference
import io.bashpsk.datastoreui.extension.setPreference
import io.bashpsk.datastoreui.resources.DatastoreUIDefaults
import kotlinx.coroutines.launch

@Composable
fun SwitchPreference(
    modifier: Modifier = Modifier,
    key: () -> Preferences.Key<Boolean>,
    initialValue: () -> Boolean = { false },
    title: () -> String,
    summary: () -> String = { "" },
    leadingContent: @Composable (() -> Unit) = {},
    colors: ListItemColors = ListItemDefaults.colors(),
    tonalElevation: Dp = ListItemDefaults.Elevation,
    shadowElevation: Dp = ListItemDefaults.Elevation,
    @FloatRange(from = 0.0, 1.0)
    summaryAlpha: Float = DatastoreUIDefaults.SUMMARY_ALPHA
) {

    val dataStore = LocalDatastore.current
    val coroutineScope = rememberCoroutineScope()

    val getSwitchState by dataStore.getPreference(
        key = key(),
        initial = initialValue()
    ).collectAsStateWithLifecycle(initialValue = initialValue())

    ListItem(
        modifier = modifier
            .clickable(
                role = Role.Checkbox,
                onClick = {

                    coroutineScope.launch {

                        dataStore.setPreference(key = key(), value = getSwitchState.not())
                    }
                }
            ),
        colors = colors,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        leadingContent = leadingContent,
        trailingContent = {

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
        headlineContent = {

            Text(
                text = title(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        supportingContent = {

            Text(
                modifier = modifier.alpha(alpha = summaryAlpha),
                text = summary(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelSmall
            )
        }
    )
}