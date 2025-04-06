package io.bash_psk.datastore_ui.preference

import androidx.annotation.FloatRange
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bash_psk.datastore_ui.extension.LocalDatastore
import io.bash_psk.datastore_ui.extension.getPreference
import io.bash_psk.datastore_ui.extension.setPreference
import io.bash_psk.datastore_ui.resources.DatastoreUIDefaults

@Composable
fun <K, V> DropDownPreference(
    modifier: Modifier = Modifier,
    keyString: () -> String,
    initialValue: () -> V,
    entities: () -> Map<K, V> = { emptyMap() },
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

    val getSelectedItem by dataStore.getPreference(
        keyString = keyString(),
        initial = initialValue()
    ).collectAsStateWithLifecycle(initialValue = initialValue())

    val isMenuExpanded = rememberSaveable {
        mutableStateOf(value = false)
    }

    val menuArrowDegree by rememberSaveable(isMenuExpanded.value) {
        mutableFloatStateOf(
            value = when (isMenuExpanded.value) {

                true -> 180.0F
                false -> 0.0F
            }
        )
    }

    ListItem(
        modifier = modifier
            .clickable(
                role = Role.Button,
                onClick = {

                    isMenuExpanded.value = true
                }
            ),
        colors = colors,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        leadingContent = leadingContent,
        trailingContent = {

            Icon(
                modifier = Modifier.rotate(degrees = menuArrowDegree),
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Drop Down Menu"
            )

            DropdownMenu(
                expanded = isMenuExpanded.value,
                onDismissRequest = {

                    isMenuExpanded.value = false
                }
            ) {

                entities().forEach { itemMenu ->

                    val isSelected by rememberSaveable(itemMenu, getSelectedItem) {
                        mutableStateOf(value = getSelectedItem == itemMenu.value)
                    }

                    DropdownMenuItem(
                        text = {

                            Text(
                                text = "${itemMenu.key}",
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        trailingIcon = {

                            AnimatedVisibility(
                                visible = isSelected,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {

                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Selected"
                                )
                            }
                        },
                        onClick = {

                            dataStore.setPreference(keyString = keyString(), value = itemMenu.value)
                            isMenuExpanded.value = false
                        }
                    )
                }
            }
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