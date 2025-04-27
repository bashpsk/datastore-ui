package io.bashpsk.datastoreui.preference

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bashpsk.datastoreui.color.ColorPickerDialog
import io.bashpsk.datastoreui.extension.LocalDatastore
import io.bashpsk.datastoreui.extension.getPreference
import io.bashpsk.datastoreui.extension.setPreference
import io.bashpsk.datastoreui.resources.DatastoreUIDefaults
import kotlinx.coroutines.launch

@Composable
fun ColorPickPreference(
    modifier: Modifier = Modifier,
    key: () -> Preferences.Key<Int>,
    @ColorInt
    initialValue: () -> Int = { Color.Unspecified.toArgb() },
    title: () -> String,
    summary: () -> String = { "" },
    leadingContent: @Composable (() -> Unit) = {},
    colors: ListItemColors = ListItemDefaults.colors(),
    tonalElevation: Dp = ListItemDefaults.Elevation,
    shadowElevation: Dp = ListItemDefaults.Elevation,
    isAlphaPanel: () -> Boolean = { false },
    @FloatRange(from = 0.0, 1.0)
    summaryAlpha: Float = DatastoreUIDefaults.SUMMARY_ALPHA
) {

    val dataStore = LocalDatastore.current
    val coroutineScope = rememberCoroutineScope()
    val dialogVisibleState = remember { MutableTransitionState(initialState = false) }

    val getColorArgb by dataStore.getPreference(
        key = key(),
        initial = initialValue()
    ).collectAsStateWithLifecycle(initialValue = initialValue())

    ColorPickerDialog(
        dialogVisibleState = dialogVisibleState,
        isAlphaPanel = isAlphaPanel(),
        onSelectedColor = { color ->

            coroutineScope.launch { dataStore.setPreference(key = key(), value = color.toArgb()) }
        }
    )

    ListItem(
        modifier = modifier
            .clickable(
                role = Role.Button,
                onClick = {

                    dialogVisibleState.targetState = true
                }
            ),
        colors = colors,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        leadingContent = leadingContent,
        trailingContent = {

            Box(
                modifier = Modifier
                    .size(width = 24.dp, height = 24.dp)
                    .clip(shape = MaterialTheme.shapes.extraSmall)
                    .border(
                        width = 0.8.dp,
                        color = MaterialTheme.colorScheme.surfaceTint,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .background(color = Color(color = getColorArgb))
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