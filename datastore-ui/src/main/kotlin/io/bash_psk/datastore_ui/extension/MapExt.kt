package io.bash_psk.datastore_ui.extension

fun <K, V> Map<K, V>.toReversMap(): Map<V, K> {

    return entries.associate { (key, value) -> value to key }
}