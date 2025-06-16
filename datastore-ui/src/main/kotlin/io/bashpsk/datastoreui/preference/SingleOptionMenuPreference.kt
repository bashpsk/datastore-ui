package io.bashpsk.datastoreui.preference

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bashpsk.datastoreui.extension.LocalDatastore
import io.bashpsk.datastoreui.extension.getPreference
import io.bashpsk.datastoreui.extension.setPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun <K, V> SingleOptionMenuPreference(
    modifier: Modifier = Modifier,
    key: () -> Preferences.Key<V>,
    initialValue: () -> V,
    entities: () -> Map<K, V> = { emptyMap() },
    title: () -> String,
    leadingContent: @Composable (() -> Unit) = {},
    trailingContent: @Composable (() -> Unit) = {},
    colors: MenuItemColors = MenuDefaults.itemColors(),
    isDismissOnBackPress: Boolean = true,
    isDismissOnClickOutside: Boolean = true,
    onMenuDismiss: () -> Unit = {}
) {

    val dataStore = LocalDatastore.current
    val coroutineScope = rememberCoroutineScope()
    val dialogVisibleState = remember { MutableTransitionState(initialState = false) }

    val getSelectedItem by dataStore.getPreference(
        key = key(),
        initial = initialValue()
    ).collectAsStateWithLifecycle(initialValue = initialValue())

    AnimatedVisibility(
        visible = dialogVisibleState.targetState,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        AlertDialog(
            modifier = Modifier.fillMaxWidth(fraction = 0.90F),
            onDismissRequest = {

                dialogVisibleState.targetState = false
                onMenuDismiss()
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
                            onMenuDismiss()
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

                    items(items = entities().toList()) { item ->

                        val isSelected by remember {
                            derivedStateOf { getSelectedItem == item.second }
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
                                                value = item.second
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
                                text = "${item.first}",
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            },
            confirmButton = {

                Button(
                    onClick = {

                        dialogVisibleState.targetState = false
                        onMenuDismiss()
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

    DropdownMenuItem(
        modifier = modifier,
        colors = colors,
        text = {

            PreferenceTitle(modifier = Modifier.wrapContentWidth(), title = title)
        },
        leadingIcon = leadingContent,
        trailingIcon = trailingContent,
        onClick = {

            dialogVisibleState.targetState = true
        }
    )
}