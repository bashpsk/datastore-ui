package io.bashpsk.datastoreui.color

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Composable
fun rememberColorPickerState(
    initialColor: Color = Color.Black,
    enableAlphaPanel: Boolean = false
): ColorPickerState {

    return remember(enableAlphaPanel, initialColor) {
        ColorPickerState(initialColor = initialColor, isAlphaPanelEnabled = enableAlphaPanel)
    }
}

@Stable
class ColorPickerState(
    val initialColor: Color,
    val isAlphaPanelEnabled: Boolean = false
) {

    var selectedColor by mutableStateOf(initialColor)
        private set

    internal var hueValue by mutableFloatStateOf(0F)
        private set

    internal var saturationValue by mutableFloatStateOf(0F)
        private set

    internal var lightnessValue by mutableFloatStateOf(0F)
        private set

    internal var alphaValue by mutableFloatStateOf(initialColor.alpha)
        private set

    init {

        val hslComponents = initialColor.toHslComponents()

        hueValue = hslComponents[0]
        saturationValue = hslComponents[1]
        lightnessValue = hslComponents[2]
    }

    fun updateColor(color: Color) {

        val hslComponents = color.toHslComponents()

        selectedColor = color
        hueValue = hslComponents[0]
        saturationValue = hslComponents[1]
        lightnessValue = hslComponents[2]
        alphaValue = color.alpha
    }

    fun updateHslA(hue: Float, saturation: Float, lightness: Float, alpha: Float) {

        hueValue = hue.coerceIn(range = 0F..360F)
        saturationValue = saturation.coerceIn(range = 0F..1F)
        lightnessValue = lightness.coerceIn(range = 0F..1F)
        alphaValue = alpha.coerceIn(range = 0F..1F)
        selectedColor = Color.hsl(hueValue, saturationValue, lightnessValue, alphaValue)
    }
}