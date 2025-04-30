package io.bashpsk.datastoreui.preference

import androidx.annotation.FloatRange
import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bashpsk.datastoreui.extension.LocalDatastore
import io.bashpsk.datastoreui.extension.getPreference
import io.bashpsk.datastoreui.extension.setPreference
import io.bashpsk.datastoreui.resources.DatastoreUIDefaults
import kotlinx.coroutines.launch

@Composable
fun CheckBoxPreference(
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

    val getChecked by dataStore.getPreference(
        key = key(),
        initial = initialValue()
    ).collectAsStateWithLifecycle(initialValue = initialValue())

    ListItem(
        modifier = modifier
            .clickable(
                role = Role.Checkbox,
                onClick = {

                    coroutineScope.launch {

                        dataStore.setPreference(key = key(), value = getChecked.not())
                    }
                }
            ),
        colors = colors,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        leadingContent = leadingContent,
        trailingContent = {

            Checkbox(checked = getChecked, onCheckedChange = null)
        },
        headlineContent = {

            PreferenceTitle(title = title)
        },
        supportingContent = {

            PreferenceSummary(summary = summary, alpha = summaryAlpha)
        }
    )
}