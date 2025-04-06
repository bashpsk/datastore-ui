package io.bash_psk.datastore_ui.font

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import io.bash_psk.datastore_ui.extension.toReverseMap

@Composable
fun rememberFontRes(id: String, entities: Map<Int, String>): MutableState<Int?> {

    val reverseEntities = remember(key1 = entities) { entities.toReverseMap() }

    return remember(key1 = id, key2 = reverseEntities) { mutableStateOf(reverseEntities[id]) }
}