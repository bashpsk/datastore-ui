package io.bashpsk.datastoreui.extension

fun <K, V> Map<K, V>.toReverseMap(): Map<V, K> {

    return entries.associate { (key, value) -> value to key }
}