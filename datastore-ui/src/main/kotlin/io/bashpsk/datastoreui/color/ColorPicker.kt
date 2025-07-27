package io.bashpsk.datastoreui.color

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.bashpsk.emptyformat.EmptyFormat

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    state: ColorPickerState = rememberColorPickerState()
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {

        SaturationLightnessPanel(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 1.0F),
            hueValue = state.hueValue,
            saturationValue = state.saturationValue,
            lightnessValue = state.lightnessValue,
            onSelectionChanged = { saturation, lightness ->

                state.updateHslA(
                    hue = state.hueValue,
                    saturation = saturation,
                    lightness = lightness,
                    alpha = state.alphaValue
                )
            }
        )

        HuePanel(
            modifier = Modifier.fillMaxWidth(),
            currentHue = state.hueValue,
            onHueChanged = { newHue ->

                state.updateHslA(
                    hue = newHue,
                    saturation = state.saturationValue,
                    lightness = state.lightnessValue,
                    alpha = state.alphaValue
                )
            }
        )

        if (state.isAlphaPanelEnabled) {

            AlphaPanel(
                modifier = Modifier.fillMaxWidth(),
                currentAlpha = state.alphaValue,
                baseColor = Color.hsl(
                    hue = state.hueValue,
                    saturation = state.saturationValue,
                    lightness = state.lightnessValue
                ),
                onAlphaChanged = { newAlpha ->

                    state.updateHslA(
                        hue = state.hueValue,
                        saturation = state.saturationValue,
                        lightness = state.lightnessValue,
                        alpha = newAlpha
                    )
                }
            )
        }

        ColorPreview(
            modifier = Modifier,
            color = state.selectedColor
        )
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun SaturationLightnessPanel(
    modifier: Modifier = Modifier,
    hueValue: Float,
    saturationValue: Float,
    lightnessValue: Float,
    onSelectionChanged: (saturation: Float, lightness: Float) -> Unit
) {

    BoxWithConstraints(
        modifier = modifier.padding(horizontal = 12.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {

        val panelWidth = constraints.maxWidth.toFloat()
        val panelHeight = constraints.maxHeight.toFloat()

        val thumbPosition = Offset(
            x = saturationValue * panelWidth,
            y = (1F - lightnessValue) * panelHeight
        )

        val thumbColor = MaterialTheme.colorScheme.onSurfaceVariant

        val thumbRadius = 10.dp
        val thumbWidth = 2.4.dp

        val tapPointerInput = Modifier.pointerInput(panelWidth, panelHeight) {

            detectTapGestures(
                onPress = { offset ->

                    val newSaturation = (offset.x / panelWidth).coerceIn(range = 0F..1F)
                    val newLightness = (1F - (offset.y / panelHeight)).coerceIn(range = 0F..1F)

                    onSelectionChanged(newSaturation, newLightness)
                }
            )
        }

        val dragPointerInput = Modifier.pointerInput(panelWidth, panelHeight) {

            detectDragGestures { change, _ ->

                val newX = (change.position.x).coerceIn(0F..panelWidth)
                val newY = (change.position.y).coerceIn(0F..panelHeight)
                val newSaturation = (newX / panelWidth).coerceIn(range = 0F..1F)
                val newLightness = (1F - (newY / panelHeight)).coerceIn(range = 0F..1F)

                onSelectionChanged(newSaturation, newLightness)
                change.consume()
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .then(tapPointerInput)
                .then(dragPointerInput)
                .clip(shape = MaterialTheme.shapes.extraSmall)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.65F),
                    shape = MaterialTheme.shapes.extraSmall
                ),
            contentDescription = "Saturation Lightness Panel"
        ) {

            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.hsl(hueValue, 0F, 0.5F), Color.hsl(hueValue, 1F, 0.5F))
                )
            )

            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color.Transparent),
                    startY = 0F,
                    endY = center.y
                )
            )

            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = center.y,
                    endY = size.height
                )
            )

            drawDragHandle(
                position = thumbPosition,
                radius = thumbRadius,
                color = thumbColor,
                width = thumbWidth
            )
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun HuePanel(
    modifier: Modifier = Modifier,
    currentHue: Float,
    onHueChanged: (Float) -> Unit
) {

    val density = LocalDensity.current

    val hueColors = remember {
        (0..359).map { hue ->
            Color.hsl(hue = hue.toFloat(), saturation = 1F, lightness = 0.5F)
        } + Color.hsl(0F, 1F, 0.5F)
    }

    val thumbColor = MaterialTheme.colorScheme.onSurfaceVariant
    val panelBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.65F)

    val trackHeight = 32.dp
    val thumbRadius = trackHeight / 2
    val thumbWidth = 2.4.dp

    val trackHeightPx = with(density) { trackHeight.toPx() }
    val thumbRadiusPx = with(density) { thumbRadius.toPx() }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {

        Text(
            text = "Hue : ${currentHue.toInt()}°",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = (thumbRadius * 2) + (thumbWidth * 2)),
            contentAlignment = Alignment.Center
        ) {

            val panelWidth = constraints.maxWidth.toFloat()

            val currentThumbX = remember(currentHue, panelWidth, thumbRadiusPx) {

                val hueStart = currentHue.coerceIn(0F..360F) - (0F..360F).start
                val hueRange = (0F..360F).endInclusive - (0F..360F).start
                val normalizedHue = hueStart / hueRange
                val hueSliderWidth = panelWidth - (2 * thumbRadiusPx)

                (normalizedHue * hueSliderWidth) + thumbRadiusPx
            }


            val dragPointerInput = Modifier.pointerInput(panelWidth, thumbRadiusPx) {

                detectDragGestures { change, _ ->

                    val newX = change.position.x.coerceIn(
                        range = thumbRadiusPx..panelWidth - thumbRadiusPx
                    )

                    val minHue = (0F..360F).start
                    val maxHue = (0F..360F).endInclusive
                    val sliderWidth = panelWidth - (2 * thumbRadiusPx)
                    val normalizedPosition = (newX - thumbRadiusPx) / sliderWidth
                    val newValue = minHue + (normalizedPosition * (maxHue - minHue))

                    onHueChanged(newValue.coerceIn(range = 0F..360F))
                    change.consume()
                }
            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .then(dragPointerInput),
                contentDescription = "Hue Panel"
            ) {

                val centerY = size.height / 2F
                val trackStartX = thumbRadiusPx
                val trackEndX = size.width - thumbRadiusPx
                val cornerRadius = 4.dp.toPx()

                drawRoundRect(
                    topLeft = Offset(trackStartX, centerY - (trackHeightPx / 2)),
                    size = Size(width = trackEndX - trackStartX, height = trackHeightPx),
                    brush = Brush.horizontalGradient(colors = hueColors),
                    cornerRadius = CornerRadius(x = cornerRadius, y = cornerRadius)
                )

                drawRoundRect(
                    topLeft = Offset(trackStartX, centerY - (trackHeightPx / 2)),
                    size = Size(width = trackEndX - trackStartX, height = trackHeightPx),
                    color = panelBorderColor,
                    cornerRadius = CornerRadius(x = cornerRadius, y = cornerRadius),
                    style = Stroke(width = 0.6.dp.toPx())
                )

                val thumbPosition = Offset(currentThumbX, centerY)

                drawDragHandle(
                    position = thumbPosition,
                    radius = thumbRadius,
                    color = thumbColor,
                    width = thumbWidth
                )
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun AlphaPanel(
    modifier: Modifier = Modifier,
    currentAlpha: Float,
    baseColor: Color,
    onAlphaChanged: (Float) -> Unit
) {

    val density = LocalDensity.current

    val thumbColor = MaterialTheme.colorScheme.onSurfaceVariant
    val panelBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.65F)

    val trackHeight = 32.dp
    val thumbRadius = trackHeight / 2
    val thumbWidth = 2.4.dp

    val trackHeightPx = with(density) { trackHeight.toPx() }
    val thumbRadiusPx = with(density) { thumbRadius.toPx() }

    val cellColorLight = Color.White
    val cellColorDark = Color.LightGray

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {

        Text(
            text = "Alpha : ${(currentAlpha * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = (thumbRadius * 2) + (thumbWidth * 2)),
            contentAlignment = Alignment.Center
        ) {

            val panelWidth = constraints.maxWidth.toFloat()

            val currentThumbX = remember(currentAlpha, panelWidth, thumbRadiusPx) {

                val alphaStart = currentAlpha.coerceIn(0F..1F) - (0F..1F).start
                val alphaRange = (0F..1F).endInclusive - (0F..1F).start
                val normalizedAlpha = alphaStart / alphaRange
                val alphaSliderWidth = panelWidth - (2 * thumbRadiusPx)

                (normalizedAlpha * alphaSliderWidth) + thumbRadiusPx
            }

            val dragPointerInput = Modifier.pointerInput(panelWidth, thumbRadiusPx) {

                detectDragGestures { change, _ ->

                    val newX = change.position.x.coerceIn(
                        range = thumbRadiusPx..panelWidth - thumbRadiusPx
                    )

                    val minAlpha = (0F..1F).start
                    val maxAlpha = (0F..1F).endInclusive
                    val sliderWidth = panelWidth - (2 * thumbRadiusPx)
                    val normalizedPosition = (newX - thumbRadiusPx) / sliderWidth
                    val newValue = minAlpha + (normalizedPosition * (maxAlpha - minAlpha))

                    onAlphaChanged(newValue.coerceIn(range = 0F..1F))
                    change.consume()
                }
            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .then(dragPointerInput),
                contentDescription = "Alpha Panel"
            ) {

                val trackActualHeight = trackHeightPx.coerceAtMost(maximumValue = size.height)
                val trackTopY = (size.height - trackActualHeight) / 2F
                val trackBottomY = trackTopY + trackActualHeight

                val trackStartX = thumbRadiusPx
                val trackEndX = size.width - thumbRadiusPx
                val trackWidth = trackEndX - trackStartX
                val cornerRadius = 4.dp.toPx()
                val checkerSizePx = trackActualHeight / 3F

                val clipPath = Path().apply {

                    val cellRect = RoundRect(
                        rect = Rect(
                            left = trackStartX,
                            top = trackTopY,
                            right = trackEndX,
                            bottom = trackBottomY
                        ),
                        cornerRadius = CornerRadius(x = cornerRadius, y = cornerRadius)
                    )

                    addRoundRect(roundRect = cellRect)
                }

                clipPath(path = clipPath) {

                    val rowCount = 3
                    val columnCount = (trackWidth / checkerSizePx).toInt() + 2

                    (0 until rowCount).forEach { rowIndex ->

                        (0 until columnCount).forEach { columnIndex ->

                            val rectLeft = trackStartX + columnIndex * checkerSizePx
                            val rectTop = trackTopY + rowIndex * checkerSizePx


                            if (rectLeft < trackEndX && rectTop < trackBottomY) {

                                val cellColor = when {

                                    (rowIndex + columnIndex) % 2 == 0 -> cellColorLight
                                    else -> cellColorDark
                                }

                                drawRect(
                                    color = cellColor,
                                    topLeft = Offset(rectLeft, rectTop),
                                    size = Size(width = checkerSizePx, height = checkerSizePx)
                                )
                            }
                        }
                    }
                }

                drawRoundRect(
                    topLeft = Offset(trackStartX, trackTopY),
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            baseColor.copy(alpha = 0F),
                            baseColor.copy(alpha = 1F)
                        )
                    ),
                    size = Size(width = trackWidth, height = trackActualHeight),
                    cornerRadius = CornerRadius(x = cornerRadius, y = cornerRadius)
                )

                drawRoundRect(
                    topLeft = Offset(trackStartX, trackTopY),
                    size = Size(width = trackWidth, height = trackActualHeight),
                    color = panelBorderColor,
                    cornerRadius = CornerRadius(x = cornerRadius, y = cornerRadius),
                    style = Stroke(width = 0.6.dp.toPx())
                )

                val thumbPosition = Offset(currentThumbX, size.height / 2F)

                drawDragHandle(
                    position = thumbPosition,
                    radius = thumbRadius,
                    color = thumbColor,
                    width = thumbWidth
                )
            }
        }
    }
}

@Composable
private fun ColorPreview(modifier: Modifier = Modifier, color: Color = Color.Unspecified) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = modifier
                .size(size = 64.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .clip(shape = MaterialTheme.shapes.extraSmall)
                .background(color = color)
        )

        ColorInfoPreview(
            modifier = Modifier,
            color = color
        )
    }


}

@Composable
private fun ColorInfoPreview(modifier: Modifier = Modifier, color: Color = Color.Unspecified) {

    val hexColorInfo by remember(color) {
        derivedStateOf { Pair(first = "HEX", second = EmptyFormat.toColorHex(color = color)) }
    }

    val argbColorInfo by remember(color) {
        derivedStateOf {
            Pair(
                first = "ARGB",
                second = "${(color.alpha * 255).toInt()}    ${(color.red * 255).toInt()}    ${
                    (color.green * 255).toInt()
                }   ${(color.blue * 255).toInt()}"
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {

        ColorInfoItem(modifier = Modifier.fillMaxWidth(), infoItem = argbColorInfo)
        ColorInfoItem(modifier = Modifier.fillMaxWidth(), infoItem = hexColorInfo)
    }
}

@Composable
private fun ColorInfoItem(modifier: Modifier = Modifier, infoItem: Pair<String, String>) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier.weight(weight = 0.35F),
            text = infoItem.first,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = ":",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.weight(weight = 1.60F),
            text = infoItem.second,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun DrawScope.drawDragHandle(position: Offset, radius: Dp, color: Color, width: Dp) {

    val stroke = Stroke(width = width.toPx())

    drawCircle(center = position, radius = radius.toPx(), style = stroke, color = color)
    drawCircle(center = position, radius = width.toPx(), color = color)
}

internal fun Color.toHslComponents(): FloatArray {

    val maxColorComponent = maxOf(red, green, blue)
    val minColorComponent = minOf(red, green, blue)
    val colorComponentDifference = maxColorComponent - minColorComponent

    var hue = 0F
    val saturation: Float
    val lightness = (maxColorComponent + minColorComponent) / 2F

    when (colorComponentDifference) {

        0F -> saturation = 0F

        else -> {

            saturation = when {

                lightness > 0.5F -> {

                    colorComponentDifference / (2F - maxColorComponent - minColorComponent)
                }

                else -> {

                    colorComponentDifference / (maxColorComponent + minColorComponent)
                }
            }

            hue = when (maxColorComponent) {

                red -> (green - blue) / colorComponentDifference + (if (green < blue) 6F else 0F)
                green -> (blue - red) / colorComponentDifference + 2F
                else -> (red - green) / colorComponentDifference + 4F
            }

            hue /= 6F
        }
    }

    return floatArrayOf(hue * 360F, saturation, lightness)
}