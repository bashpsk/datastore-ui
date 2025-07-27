package io.bashpsk.datastoreui.preference

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.bashpsk.datastoreui.resources.DatastoreUIDefaults

@Composable
internal inline fun PreferenceTitle(modifier: Modifier = Modifier, title: () -> String) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(space = 4.dp)
    ) {

        Text(
            text = title(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
internal inline fun PreferenceSummary(
    modifier: Modifier = Modifier,
    summary: () -> String = { "" },
    alpha: Float = DatastoreUIDefaults.SUMMARY_ALPHA
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(space = 4.dp)
    ) {

        Spacer(modifier = Modifier.height(height = 0.dp))

        Text(
            modifier = modifier.alpha(alpha = alpha),
            text = summary(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
internal inline fun PreferenceSummary(
    modifier: Modifier = Modifier,
    summary: () -> String = { "" },
    alpha: Float = DatastoreUIDefaults.SUMMARY_ALPHA,
    fontFamily: FontFamily
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(space = 4.dp)
    ) {

        Spacer(modifier = Modifier.height(height = 0.dp))

        Text(
            modifier = modifier.alpha(alpha = alpha),
            text = summary(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelSmall,
            fontFamily = fontFamily
        )
    }
}

@Composable
internal fun PreferenceDialogButton(
    modifier: Modifier = Modifier,
    horiArrangement: Arrangement.Horizontal = Arrangement.End,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    onDoneClick: () -> Unit
) {

    Row(
        modifier = modifier,
        horizontalArrangement = horiArrangement,
        verticalAlignment = verticalAlignment
    ) {

        Button(onClick = onDoneClick) {

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
}

@Composable
internal fun PreferenceDialogButton(
    modifier: Modifier = Modifier,
    horiArrangement: Arrangement.Horizontal = Arrangement.SpaceAround,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    onDoneClick: () -> Unit,
    onResetClick: () -> Unit
) {

    Row(
        modifier = modifier,
        horizontalArrangement = horiArrangement,
        verticalAlignment = verticalAlignment
    ) {

        OutlinedButton (onClick = onResetClick) {

            Icon(
                imageVector = Icons.Filled.Restore,
                contentDescription = "Reset"
            )

            Spacer(modifier = Modifier.width(width = 2.dp))

            Text(
                text = "Reset",
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Button(onClick = onDoneClick) {

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
}