package matt.collect.map.dmap

import matt.collect.map.dmap.inter.CanBeNotNullMutableMap

expect class DefaultStoringMap<K, V : Any>(
    map: MutableMap<K, V>,
    d: (K) -> V
) : CanBeNotNullMutableMap<K, V> {
    internal val map: MutableMap<K, V>
    internal val d: (K) -> V

    override val size: Int

    override fun containsKey(key: K): Boolean

    override fun containsValue(value: V): Boolean


    override operator fun get(key: K): V

    fun getWithoutSetting(key: K): V?

    override fun isEmpty(): Boolean

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
    override val keys: MutableSet<K>
    override val values: MutableCollection<V>

    override fun clear()

    override fun put(
        key: K,
        value: V
    ): V?

    override fun putAll(from: Map<out K, V>)

    override fun remove(key: K): V?
}
