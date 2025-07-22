package io.bashpsk.datastoreui.preference

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.bashpsk.datastoreui.resources.DatastoreUIDefaults

@Composable
internal inline fun PreferenceTitle(modifier: Modifier = Modifier, title: () -> String) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        horizontalAlignment = Alignment.Start
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
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        horizontalAlignment = Alignment.Start
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
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        horizontalAlignment = Alignment.Start
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