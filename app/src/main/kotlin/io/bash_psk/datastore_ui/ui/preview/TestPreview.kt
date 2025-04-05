package io.bash_psk.datastore_ui.ui.preview

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.bash_psk.datastore_ui.color.ColorPicker

/**
 * Demo that shows picking a color from a color wheel, which then dynamically updates the color of a
 * [TopAppBar]. This pattern could also be used to update the value of a Colors, updating the
 * overall theme for an application.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ColorPickerDemo() {

    val selectedColor = remember { mutableStateOf(value = Color.Unspecified) }

    ColorPicker(
        isAlphaPanel = true,
        onColorChange = { selectedColor.value = it }
    )
}