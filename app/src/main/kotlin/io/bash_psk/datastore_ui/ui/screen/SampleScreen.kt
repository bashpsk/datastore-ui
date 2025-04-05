package io.bash_psk.datastore_ui.ui.screen

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SdStorage
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.SwitchCamera
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bash_psk.datastore_ui.extension.LocalDatastore
import io.bash_psk.datastore_ui.extension.getPreference
import io.bash_psk.datastore_ui.extension.toReversMap
import io.bash_psk.datastore_ui.preference.CardPreference
import io.bash_psk.datastore_ui.preference.CheckBoxPreference
import io.bash_psk.datastore_ui.preference.ColorPickPreference
import io.bash_psk.datastore_ui.preference.DropDownPreference
import io.bash_psk.datastore_ui.preference.MultiOptionPreference
import io.bash_psk.datastore_ui.preference.SingleOptionMenuPreference
import io.bash_psk.datastore_ui.preference.SingleOptionPreference
import io.bash_psk.datastore_ui.preference.SliderPreference
import io.bash_psk.datastore_ui.preference.SwitchMenuPreference
import io.bash_psk.datastore_ui.preference.SwitchPreference
import io.bash_psk.datastore_ui.preference.TextFieldPreference
import io.bash_psk.datastore_ui.settings.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleScreen() {

    val context = LocalContext.current
    val dataStore = LocalDatastore.current
    val optionMenuVisibleState = remember { MutableTransitionState(initialState = false) }

    val getFieldText by dataStore.getPreference(
        keyString = "TEXT-FIELD-PREFERENCE",
        initial = ""
    ).collectAsStateWithLifecycle(initialValue = "")

    val getAppTheme by dataStore.getPreference(
        keyString = "SINGLE-OPTION-MENU-PREFERENCE",
        initial = AppTheme.SYSTEM.name
    ).collectAsStateWithLifecycle(initialValue = AppTheme.SYSTEM.name)

    val getSelectedItem by dataStore.getPreference(
        keyString = "MULTI-OPTION-PREFERENCE",
        initial = emptySet<String>()
    ).collectAsStateWithLifecycle(initialValue = emptySet<String>())

    val textFieldValue = remember { mutableStateOf(value = TextFieldValue(text = getFieldText)) }

    val sampleEntities = mapOf("One" to "Kotlin", "Two" to "Bash PSK", "Three" to "Empty Layer")

    val themeEntities = AppTheme.entries.associate { theme ->

        theme.name.lowercase().replaceFirstChar { char -> char.uppercaseChar() } to theme.name
    }

    val reversEntities = remember(key1 = sampleEntities) {
        sampleEntities.toReversMap()
    }

    val summaryItems = remember(getSelectedItem, reversEntities) {
        getSelectedItem.mapNotNull { item -> reversEntities[item] }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

            TopAppBar(
                title = {

                    Text(
                        text = "Datastore UI",
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {

                    IconButton(
                        onClick = {

                            optionMenuVisibleState.targetState = true
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Option Menu"
                        )
                    }

                    DropdownMenu(
                        expanded = optionMenuVisibleState.targetState,
                        onDismissRequest = {

                            optionMenuVisibleState.targetState = false
                        }
                    ) {

                        SingleOptionMenuPreference(
                            keyString = { "SINGLE-OPTION-MENU-PREFERENCE" },
                            initialValue = { AppTheme.SYSTEM.name },
                            entities = { themeEntities },
                            title = { "App Theme" },
                            leadingContent = {

                                when (AppTheme.valueOf(getAppTheme)) {

                                    AppTheme.SYSTEM -> Icon(
                                        imageVector = Icons.Filled.BrightnessAuto,
                                        contentDescription = "App Theme"
                                    )

                                    AppTheme.DARK -> Icon(
                                        imageVector = Icons.Filled.Brightness2,
                                        contentDescription = "App Theme"
                                    )

                                    AppTheme.LIGHT -> Icon(
                                        imageVector = Icons.Filled.Brightness6,
                                        contentDescription = "App Theme"
                                    )
                                }
                            },
                            onMenuDismiss = {

                                optionMenuVisibleState.targetState = false
                            }
                        )

                        SwitchMenuPreference(
                            keyString = { "SWITCH-MENU-PREFERENCE" },
                            initialValue = { false },
                            title = { "Switch Menu" },
                            leadingContent = {

                                Icon(
                                    imageVector = Icons.Filled.Devices,
                                    contentDescription = ""
                                )
                            },
                            onMenuDismiss = {

                                optionMenuVisibleState.targetState = false
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            columns = GridCells.Adaptive(minSize = 320.dp),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {

            item { HorizontalDivider() }

            item {

                CardPreference(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    title = { "Card Preference" },
                    summary = { "Select video download path." },
                    leadingContent = {

                        Icon(
                            imageVector = Icons.Filled.SdStorage,
                            contentDescription = ""
                        )
                    },
                    trailingContent = {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = ""
                        )
                    }
                )
            }

            item { HorizontalDivider() }

            item {

                CheckBoxPreference(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    keyString = { "CHECK-BOX-PREFERENCE" },
                    initialValue = { false },
                    title = { "Check Box Preference" },
                    summary = { "Enable the check box preference." },
                    leadingContent = {

                        Icon(
                            imageVector = Icons.Filled.SelectAll,
                            contentDescription = ""
                        )
                    }
                )
            }

            item { HorizontalDivider() }

            item {

                ColorPickPreference(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    keyString = { "COLOR-PICK-PREFERENCE" },
                    title = { "Color Picker Preference" },
                    summary = { "Select a color for color pick preference." },
                    leadingContent = {

                        Icon(
                            imageVector = Icons.Filled.Colorize,
                            contentDescription = ""
                        )
                    },
                    isAlphaPanel = { true }
                )
            }

            item { HorizontalDivider() }

            item {

                DropDownPreference(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    keyString = { "DROP-DOWN-PREFERENCE" },
                    initialValue = { "" },
                    entities = { sampleEntities },
                    title = { "Drop Down Preference" },
                    summary = { "Select one entity from the list." },
                    leadingContent = {

                        Icon(
                            imageVector = Icons.Filled.Code,
                            contentDescription = ""
                        )
                    }
                )
            }

            item { HorizontalDivider() }

            item {

                MultiOptionPreference(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    keyString = { "MULTI-OPTION-PREFERENCE" },
                    initialValue = { emptySet() },
                    entities = { sampleEntities },
                    title = { "Multiple Option Selection Preference" },
                    summary = { "Select entities from the list. $summaryItems" },
                    leadingContent = {

                        Icon(
                            imageVector = Icons.Filled.Sell,
                            contentDescription = ""
                        )
                    },
                    trailingContent = {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = ""
                        )
                    }
                )
            }

            item { HorizontalDivider() }

            item {

                SingleOptionPreference(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    keyString = { "SINGLE-OPTION-PREFERENCE" },
                    initialValue = { "" },
                    entities = { sampleEntities },
                    title = { "Single Option Selection Preference" },
                    summary = { "Select one entity from the list." },
                    leadingContent = {

                        Icon(
                            imageVector = Icons.Filled.Gamepad,
                            contentDescription = ""
                        )
                    },
                    trailingContent = {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = ""
                        )
                    }
                )
            }

            item { HorizontalDivider() }

            item {

                SliderPreference(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    keyString = { "SLIDER-PREFERENCE" },
                    initialValue = { 0.0F },
                    title = { "Slider Preference" },
                    summary = { "Adjust slider value." },
                    leadingContent = {

                        Icon(
                            imageVector = Icons.Filled.Place,
                            contentDescription = ""
                        )
                    },
                    valueRange = 0.0F..1.0F,
                    isValueVisible = true,
                    decimalFraction = 1
                )
            }

            item { HorizontalDivider() }

            item {

                SliderPreference(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    keyString = { "SLIDER-STEP-PREFERENCE" },
                    initialValue = { 0.0F },
                    title = { "Slider Step Preference" },
                    summary = { "Adjust slider value from 0 to 50." },
                    leadingContent = {

                        Icon(
                            imageVector = Icons.Filled.PinDrop,
                            contentDescription = ""
                        )
                    },
                    isValueVisible = false,
                    valueRange = 0.0F..50.0F,
                    steps = 9
                )
            }

            item { HorizontalDivider() }

            item {

                SwitchPreference(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    keyString = { "SWITCH-PREFERENCE" },
                    initialValue = { false },
                    title = { "Switch Preference" },
                    summary = { "Enable the switch preference." },
                    leadingContent = {

                        Icon(
                            imageVector = Icons.Filled.SwitchCamera,
                            contentDescription = ""
                        )
                    }
                )
            }

            item { HorizontalDivider() }

            item {

                TextFieldPreference(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 100),
                        placementSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioMediumBouncy
                        )
                    ),
                    keyString = { "TEXT-FIELD-PREFERENCE" },
                    initialValue = { "" },
                    title = { "Text Field Preference" },
                    summary = { "Enter a text field preference." },
                    leadingContent = {

                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = ""
                        )
                    },
                    trailingContent = {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = ""
                        )
                    },
                    textFieldValue = textFieldValue.value,
                    textFieldContent = {

                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = textFieldValue.value,
                            onValueChange = { newInput ->

                                textFieldValue.value = newInput
                            },
                            singleLine = true,
                            label = {

                                Text(text = "User Name")
                            },
                            leadingIcon = {

                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = "User"
                                )
                            },
                            trailingIcon = {

                                IconButton(
                                    onClick = {

                                        textFieldValue.value = textFieldValue.value.copy(text = "")
                                    }
                                ) {

                                    Icon(
                                        imageVector = Icons.Filled.ClearAll,
                                        contentDescription = "Clear"
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                autoCorrectEnabled = false,
                                keyboardType = KeyboardType.Text
                            )
                        )
                    }
                )
            }

            item { HorizontalDivider() }
        }
    }
}