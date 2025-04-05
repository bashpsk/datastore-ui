package io.bash_psk.datastore_ui.preference

import androidx.annotation.FloatRange
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Label
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bash_psk.datastore_ui.extension.LocalDatastore
import io.bash_psk.datastore_ui.extension.getPreference
import io.bash_psk.datastore_ui.extension.setPreference
import io.bash_psk.datastore_ui.resources.DatastoreUIDefaults
import io.bash_psk.empty_format.EmptyFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderPreference(
    modifier: Modifier = Modifier,
    keyString: () -> String,
    initialValue: () -> Float = { 0.0F },
    title: () -> String,
    summary: () -> String = { "" },
    leadingContent: @Composable (() -> Unit) = {},
    colors: ListItemColors = ListItemDefaults.colors(),
    tonalElevation: Dp = ListItemDefaults.Elevation,
    shadowElevation: Dp = ListItemDefaults.Elevation,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    isValueVisible: Boolean = false,
    decimalFraction: Int = 1,
    sliderColors: SliderColors = SliderDefaults.colors(),
    thumbSize: DpSize = DpSize(width = 16.0.dp, height = 16.0.dp),
    thumbTrackGapSize: Dp = 4.dp,
    trackInsideCornerSize: Dp = 2.dp,
    trackThickness: Dp = 4.dp,
    @FloatRange(from = 0.0, 1.0)
    summaryAlpha: Float = DatastoreUIDefaults.SUMMARY_ALPHA
) {

    val dataStore = LocalDatastore.current
    val sliderInteractionSource = remember { MutableInteractionSource() }

    val getPosition by dataStore.getPreference(
        keyString = keyString(),
        initial = initialValue()
    ).collectAsStateWithLifecycle(initialValue = initialValue())

    ListItem(
        modifier = modifier,
        colors = colors,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        leadingContent = leadingContent,
        trailingContent = {

            AnimatedVisibility(
                visible = isValueVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                Text(
                    text = "${
                        EmptyFormat.toRoundedDecimal(
                            decimal = getPosition,
                            fraction = decimalFraction
                        )
                    }",
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium,
                    overflow = TextOverflow.Ellipsis
                )
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

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(space = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(alpha = summaryAlpha),
                    text = summary(),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelSmall
                )

                Slider(
                    modifier = Modifier.fillMaxWidth(),
                    value = getPosition,
                    valueRange = valueRange,
                    steps = steps,
                    onValueChange = { position ->

                        dataStore.setPreference(keyString = keyString(), value = position)
                    },
                    colors = sliderColors,
                    interactionSource = sliderInteractionSource,
                    thumb = { sliderState: SliderState ->

                        Label(
                            interactionSource = sliderInteractionSource,
                            label = {

                                RichTooltip {

                                    Text(
                                        text = "${
                                            EmptyFormat.toRoundedDecimal(
                                                decimal = getPosition,
                                                fraction = decimalFraction
                                            )
                                        }",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.labelSmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        ) {

                            SliderDefaults.Thumb(
                                interactionSource = sliderInteractionSource,
                                thumbSize = thumbSize,
                                colors = sliderColors
                            )
                        }
                    },
                    track = { sliderState: SliderState ->

                        SliderDefaults.Track(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height = trackThickness),
                            sliderState = sliderState,
                            colors = sliderColors,
                            thumbTrackGapSize = thumbTrackGapSize,
                            trackInsideCornerSize = trackInsideCornerSize
                        )
                    }
                )
            }
        }
    )
}