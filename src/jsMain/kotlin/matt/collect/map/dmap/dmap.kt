package matt.collect.map.dmap

actual class DefaultStoringMap<K, V : Any> actual constructor(
    actual val map: MutableMap<K, V>,
    actual val d: (K) -> V,
) : CanBeNotNullMutableMap<K, V> {
    actual override val size: Int
        get() = map.size

    actual override fun containsKey(key: K) = map.containsKey(key)

    actual override fun containsValue(value: V) = map.containsValue(value)

    actual override operator fun get(key: K): V = map[key] ?: d(key).also {
        map[key] = it
    }

    actual fun getWithoutSetting(key: K): V? = map[key]

    actual override fun isEmpty() = map.isEmpty()

    actual override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = map.entries
    actual override val keys: MutableSet<K>
        get() = map.keys
    actual override val values: MutableCollection<V>
        get() = map.values

    actual override fun clear() = map.clear()

    actual override fun put(
        key: K,
        value: V,
    ): V? = map.put(key, value)

    actual override fun putAll(from: Map<out K, V>) = map.putAll(from)

    actual override fun remove(key: K): V? = map.remove(key)
}
