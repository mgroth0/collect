package matt.collect.cast


/*slow but safe*/
inline fun <K, V, reified R: V> Map<K, V>.castValues(): Map<K, R> = entries.associate { it.key to it.value as R }
