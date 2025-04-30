package io.bashpsk.datastoreui.preference

import androidx.annotation.FloatRange
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.datastore.preferences.core.Preferences
import io.bashpsk.datastoreui.extension.LocalDatastore
import io.bashpsk.datastoreui.extension.setPreference
import io.bashpsk.datastoreui.resources.DatastoreUIDefaults
import kotlinx.coroutines.launch

@Composable
fun TextFieldPreference(
    modifier: Modifier = Modifier,
    key: () -> Preferences.Key<String>,
    title: () -> String,
    summary: () -> String = { "" },
    leadingContent: @Composable (() -> Unit) = {},
    trailingContent: @Composable (() -> Unit) = {},
    colors: ListItemColors = ListItemDefaults.colors(),
    tonalElevation: Dp = ListItemDefaults.Elevation,
    shadowElevation: Dp = ListItemDefaults.Elevation,
    isDismissOnBackPress: Boolean = true,
    isDismissOnClickOutside: Boolean = true,
    textFieldValue: TextFieldValue,
    textFieldContent: @Composable (() -> Unit) = {},
    @FloatRange(from = 0.0, 1.0)
    summaryAlpha: Float = DatastoreUIDefaults.SUMMARY_ALPHA
) {

    val dataStore = LocalDatastore.current
    val coroutineScope = rememberCoroutineScope()
    val dialogVisibleState = remember { MutableTransitionState(initialState = false) }

    AnimatedVisibility(visibleState = dialogVisibleState) {

        AlertDialog(
            modifier = Modifier.fillMaxWidth(fraction = 0.90F),
            onDismissRequest = {

                dialogVisibleState.targetState = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = isDismissOnBackPress,
                dismissOnClickOutside = isDismissOnClickOutside
            ),
            shape = MaterialTheme.shapes.small,
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
            text = textFieldContent,
            confirmButton = {

                Button(
                    onClick = {

                        coroutineScope.launch {

                            dataStore.setPreference(
                                key = key(),
                                value = textFieldValue.text
                            )
                        }

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
            },
            dismissButton = {

                OutlinedButton(
                    onClick = {

                        dialogVisibleState.targetState = false
                    }
                ) {

                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cancel"
                    )

                    Spacer(modifier = Modifier.width(width = 2.dp))

                    Text(
                        text = "Cancel",
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

@Composable
fun <V> TextFieldPreference(
    modifier: Modifier = Modifier,
    key: () -> Preferences.Key<V>,
    title: () -> String,
    summary: () -> String = { "" },
    leadingContent: @Composable (() -> Unit) = {},
    trailingContent: @Composable (() -> Unit) = {},
    colors: ListItemColors = ListItemDefaults.colors(),
    tonalElevation: Dp = ListItemDefaults.Elevation,
    shadowElevation: Dp = ListItemDefaults.Elevation,
    isDismissOnBackPress: Boolean = true,
    isDismissOnClickOutside: Boolean = true,
    textFieldValue: V,
    textFieldContent: @Composable (() -> Unit) = {},
    @FloatRange(from = 0.0, 1.0)
    summaryAlpha: Float = DatastoreUIDefaults.SUMMARY_ALPHA
) {

    val dataStore = LocalDatastore.current
    val coroutineScope = rememberCoroutineScope()
    val dialogVisibleState = remember { MutableTransitionState(initialState = false) }

    AnimatedVisibility(visibleState = dialogVisibleState) {

        AlertDialog(
            modifier = Modifier.fillMaxWidth(fraction = 0.90F),
            onDismissRequest = {

                dialogVisibleState.targetState = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = isDismissOnBackPress,
                dismissOnClickOutside = isDismissOnClickOutside
            ),
            shape = MaterialTheme.shapes.small,
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
            text = textFieldContent,
            confirmButton = {

                Button(
                    onClick = {

                        coroutineScope.launch {

                            dataStore.setPreference(key = key(), value = textFieldValue)
                        }

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
            },
            dismissButton = {

                OutlinedButton(
                    onClick = {

                        dialogVisibleState.targetState = false
                    }
                ) {

                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cancel"
                    )

                    Spacer(modifier = Modifier.width(width = 2.dp))

                    Text(
                        text = "Cancel",
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

            PreferenceSummary(summary = summary, alpha = summaryAlpha)
        }
    )
}