package io.bashpsk.datastoreui.font

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.bashpsk.datastoreui.extension.toReverseMap

@Composable
fun rememberFontRes(id: String, entities: Map<Int, String>): State<Int?> {

    val reverseEntities by remember(entities) { derivedStateOf { entities.toReverseMap() } }

    return remember(reverseEntities, id) { derivedStateOf { reverseEntities[id] } }
}