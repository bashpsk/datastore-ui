package io.bashpsk.datastoreui.preference

import androidx.annotation.FloatRange
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bashpsk.datastoreui.extension.LocalDatastore
import io.bashpsk.datastoreui.extension.getPreference
import io.bashpsk.datastoreui.extension.setPreference
import io.bashpsk.datastoreui.font.rememberFontRes
import io.bashpsk.datastoreui.resources.DatastoreUIDefaults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FontPreference(
    modifier: Modifier = Modifier,
    key: () -> Preferences.Key<String>,
    initialValue: () -> String,
    entities: () -> Map<Int, String> = { emptyMap() },
    title: () -> String,
    summary: () -> String = { "" },
    previewText: () -> String = { "This is sample text. Time - 3:33 AM" },
    leadingContent: @Composable (() -> Unit) = {},
    trailingContent: @Composable (() -> Unit) = {},
    colors: ListItemColors = ListItemDefaults.colors(),
    tonalElevation: Dp = ListItemDefaults.Elevation,
    shadowElevation: Dp = ListItemDefaults.Elevation,
    isDismissOnBackPress: Boolean = true,
    isDismissOnClickOutside: Boolean = true,
    @FloatRange(from = 0.0, to = 1.0)
    summaryAlpha: Float = DatastoreUIDefaults.SUMMARY_ALPHA
) {

    val dataStore = LocalDatastore.current
    val coroutineScope = rememberCoroutineScope()

    val getSelectedItem by dataStore.getPreference(
        key = key(),
        initial = initialValue()
    ).collectAsStateWithLifecycle(initialValue = initialValue())

    val dialogVisibleState = remember { MutableTransitionState(initialState = false) }

    val selectedFontRes by rememberFontRes(id = getSelectedItem, entities = entities())

    AnimatedVisibility(visibleState = dialogVisibleState) {

        AlertDialog(
            modifier = Modifier.fillMaxWidth(fraction = 0.90f),
            onDismissRequest = {

                dialogVisibleState.targetState = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = isDismissOnBackPress,
                dismissOnClickOutside = isDismissOnClickOutside
            ),
            shape = MaterialTheme.shapes.small,
            titleContentColor = AlertDialogDefaults.titleContentColor,
            containerColor = AlertDialogDefaults.containerColor.copy(alpha = 0.70f),
            textContentColor = AlertDialogDefaults.textContentColor,
            iconContentColor = AlertDialogDefaults.iconContentColor,
            title = {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        modifier = Modifier.weight(weight = 1.0F),
                        text = title(),
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.Ellipsis
                    )

                    IconButton(
                        onClick = {

                            dialogVisibleState.targetState = false
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close"
                        )
                    }
                }
            },
            text = {

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(space = 4.dp),
                    horizontalAlignment = Alignment.Start
                ) {

                    items(items = entities().toList()) { fontItem ->

                        val isSelected by remember(getSelectedItem, fontItem) {
                            derivedStateOf { getSelectedItem == fontItem.second }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = isSelected,
                                    role = Role.RadioButton,
                                    onClick = {

                                        coroutineScope.launch(context = Dispatchers.IO) {

                                            dataStore.setPreference(
                                                key = key(),
                                                value = fontItem.second
                                            )
                                        }
                                    }
                                )
                                .padding(all = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
                        ) {

                            RadioButton(selected = isSelected, onClick = null)

                            Text(
                                text = previewText(),
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = FontFamily(Font(resId = fontItem.first))
                            )
                        }
                    }
                }
            },
            confirmButton = {

                Button(
                    onClick = {

                        dialogVisibleState.targetState = false
                    }
                ) {

                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done"
                    )

                    Spacer(modifier = Modifier.width(width = 2.dp))

                    Text(
                        text = "Done",
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        )
    }

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
        trailingContent = trailingContent,
        headlineContent = {

            PreferenceTitle(title = title)
        },
        supportingContent = {

            PreferenceSummary(
                summary = summary,
                alpha = summaryAlpha,
                fontFamily = when (selectedFontRes != null) {

                    true -> FontFamily(Font(resId = selectedFontRes!!))
                    false -> FontFamily.Default
                }
            )
        }
    )
}