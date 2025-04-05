package io.bash_psk.datastore_ui.color

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.RectF
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.SweepGradientShader
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toRect
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    isAlphaPanel: Boolean = false,
    onColorChange: (color: Color) -> Unit
) {

    val contentColor = LocalContentColor.current
    val selectedColor = remember { mutableStateOf(value = Color.Unspecified) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        BoxWithConstraints(
            modifier = Modifier
                .weight(weight = 1.0F)
                .aspectRatio(ratio = 1.0F)
                .clip(shape = CircleShape)
                .border(
                    width = 0.4.dp,
                    color = MaterialTheme.colorScheme.error,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            val diameter = constraints.maxWidth
            val position = remember { mutableStateOf(value = Offset.Zero) }
            val colorWheel = remember(key1 = diameter) { ColorWheel(diameter = diameter) }
            val hasInput = remember { mutableStateOf(value = false) }

            val pointerInputModifier = Modifier.pointerInput(key1 = colorWheel) {

                fun updateColorWheel(newPosition: Offset) {

                    val newColor = colorWheel.colorForPosition(position = newPosition)

                    when {

                        newColor.isSpecified -> {

                            position.value = newPosition
                            selectedColor.value = newColor
                            onColorChange(selectedColor.value)
                        }
                    }
                }

                awaitEachGesture {

                    val down = awaitFirstDown()

                    hasInput.value = true
                    updateColorWheel(newPosition = down.position)

                    drag(pointerId = down.id) { change ->

                        change.consume()
                        updateColorWheel(change.position)
                    }

                    hasInput.value = false
                }
            }

            Box(modifier = Modifier.matchParentSize()) {

                Image(
                    modifier = pointerInputModifier,
                    bitmap = colorWheel.image,
                    contentDescription = "Color Wheel"
                )

                Canvas(
                    modifier = Modifier.matchParentSize(),
                    contentDescription = "Color Selection Point"
                ) {

                    drawCircle(
                        color = contentColor,
                        radius = 12.dp.toPx(),
                        center = position.value,
                        style = Stroke(width = 2.dp.toPx())
                    )

                    drawCircle(
                        color = contentColor,
                        radius = 2.4.dp.toPx(),
                        center = position.value,
                        style = Fill
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(alignment = Alignment.End)
                .padding(all = 16.dp)
                .size(width = 48.dp, height = 32.dp)
                .clip(shape = MaterialTheme.shapes.small)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
                .background(color = selectedColor.value)
        )

        AlphaPanel(isAlphaPanel = isAlphaPanel, selectedColor = selectedColor.value) { alpha ->

            selectedColor.value = selectedColor.value.copy(alpha = alpha)
            onColorChange(selectedColor.value)
        }
    }
}

private class ColorWheel(diameter: Int) {

    private val colorList = persistentListOf(
        Color.Red,
        Color.Magenta,
        Color.Blue,
        Color.Black,
        Color.Green,
        Color.Yellow,
        Color.White,
        Color.Red
    )

    private val radius = diameter / 2f

    private val sweepGradient = SweepGradientShader(
        colors = colorList,
        colorStops = null,
        center = Offset(x = radius, y = radius)
    )

    val image = ImageBitmap(width = diameter, height = diameter).also { imageBitmap ->

        val canvas = Canvas(image = imageBitmap)
        val center = Offset(x = radius, y = radius)
        val paint = Paint().apply { shader = sweepGradient }

        canvas.drawCircle(center = center, radius = radius, paint = paint)
    }
}

private fun ColorWheel.colorForPosition(position: Offset): Color {

    val x = position.x.toInt().coerceAtLeast(minimumValue = 0)
    val y = position.y.toInt().coerceAtLeast(minimumValue = 0)

    with(receiver = image.toPixelMap()) {

        return when {

            x >= width || y >= height -> Color.Unspecified
            else -> this[x, y].takeIf { color -> color.alpha == 1f } ?: Color.Unspecified
        }
    }
}

@Composable
private fun AlphaPanel(
    isAlphaPanel: Boolean,
    selectedColor: Color,
    onAlphaLevel: (alpha: Float) -> Unit
) {

    val contentColor = LocalContentColor.current
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val visibleState = remember { MutableTransitionState(initialState = isAlphaPanel) }

    val pressOffset = remember { mutableStateOf(value = IntOffset.Zero) }
    val selectedAlpha = rememberSaveable { mutableFloatStateOf(value = 1f) }

    AnimatedVisibility(visibleState = visibleState) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 32.dp)
                .clip(shape = MaterialTheme.shapes.extraSmall)
                .border(
                    width = 0.4.dp,
                    color = MaterialTheme.colorScheme.error,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .emitDragGesture(interactionSource = interactionSource)
                .onPlaced(
                    onPlaced = { layoutCoordinate ->

                        pressOffset.value = IntOffset(
                            x = layoutCoordinate.size.width,
                            y = layoutCoordinate.size.height
                        )
                    }
                )
                .padding(all = 2.dp),
            contentDescription = "Alpha Panel"
        ) {

            val cellCount = 3
            val drawScopeSize = size
            val boxSize = size.height / cellCount
            val bitmap = createBitmap(width = size.width.toInt(), height = size.height.toInt())
            val huePanel = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())

            (0 .. (size.width / boxSize).toInt()).forEach { boxOne ->

                (0 until cellCount).forEach { boxTwo ->

                    drawRect(
                        color = when ((boxOne + boxTwo) % 2 == 0) {

                            true -> selectedColor.copy(alpha = selectedAlpha.floatValue)
                            false -> Color.White
                        },
                        topLeft = Offset(x = boxOne * boxSize, y = boxTwo * boxSize),
                        size = Size(width = boxSize, height = boxSize)
                    )
                }
            }

            drawBitmap(bitmap = bitmap, panel = huePanel)

            coroutineScope.pressCollector(interactionSource = interactionSource) { offset ->

                val pressPosition = offset.x.coerceIn(range = 0f..drawScopeSize.width)

                val alpha = pointAlpha(
                    pointX = pressPosition,
                    panelWidth = huePanel.width(),
                    panelLeft = huePanel.left,
                    panelRight = huePanel.right
                )

                pressOffset.value = IntOffset(x = pressPosition.toInt(), y = 0)
                selectedAlpha.floatValue = alpha
                onAlphaLevel(selectedAlpha.floatValue)
            }

            drawCircle(
                color = contentColor,
                radius = size.height / 2,
                center = pressOffset.value.toOffset().copy(y = size.height / 2),
                style = Stroke(width = 2.dp.toPx())
            )

            drawCircle(
                color = contentColor,
                radius = 2.4.dp.toPx(),
                center = pressOffset.value.toOffset().copy(y = size.height / 2),
                style = Fill
            )
        }
    }
}

private fun DrawScope.drawBitmap(bitmap: Bitmap, panel: RectF) {

    drawIntoCanvas { canvas ->

        canvas.nativeCanvas.drawBitmap(bitmap, null, panel.toRect(), null)
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.emitDragGesture(
    interactionSource: MutableInteractionSource
): Modifier = composed {

    val coroutineScope = rememberCoroutineScope()

    pointerInput(key1 = Unit) {

        detectDragGestures { inputChange: PointerInputChange, dragAmount: Offset ->

            coroutineScope.launch {

                interactionSource.emit(
                    interaction = PressInteraction.Press(pressPosition = inputChange.position)
                )
            }
        }
    }.clickable(interactionSource = interactionSource, indication = null) {}
}

private fun pointAlpha(
    pointX: Float,
    panelWidth: Float,
    panelLeft: Float,
    panelRight: Float
): Float {

    val x = when {

        pointX < panelLeft -> 0.0f
        pointX > panelRight -> panelWidth
        else -> pointX - panelLeft
    }

    return x * 1.0f / panelWidth
}

private fun CoroutineScope.pressCollector(
    interactionSource: InteractionSource,
    onOffset: (offset: Offset) -> Unit
) {

    launch {

        interactionSource.interactions.collect { interaction: Interaction ->

            (interaction as? PressInteraction.Press)?.pressPosition?.let(onOffset)
        }
    }
}