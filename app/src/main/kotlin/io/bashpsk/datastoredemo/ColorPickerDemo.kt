package io.bashpsk.datastoredemo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.bashpsk.datastoreui.color.ColorPicker
import io.bashpsk.datastoreui.color.rememberColorPickerState

@Composable
fun ColorPickerDemoScreen() {

    val state = rememberColorPickerState(enableAlphaPanel = true)

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        ColorPicker(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            state = state
        )
    }
}