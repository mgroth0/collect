package matt.collect.map.dmap.inter

import matt.collect.map.dmap.DefaultStoringMap

fun <K, V : Any> MutableMap<K, V>.withStoringDefault(
    d: (K) -> V
): DefaultStoringMap<K, V> = DefaultStoringMap(this, d)

interface CanBeNotNullMutableMap<K, V : Any> : MutableMap<K, V>, CanBeNotNullMap<K, V>
interface CanBeNotNullMap<K, V : Any> : Map<K, V> {
    override operator fun get(key: K): V
}
