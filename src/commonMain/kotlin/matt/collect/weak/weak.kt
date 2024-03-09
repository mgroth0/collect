package matt.collect.weak

import kotlin.collections.MutableMap.MutableEntry

expect class WeakMap<K, V>() : MutableMap<K, V> {
    override fun clear()

    override fun put(
        key: K,
        value: V
    ): V?

    override fun putAll(from: Map<out K, V>)

    override fun remove(key: K): V?

    override val entries: MutableSet<MutableEntry<K, V>>
    override val keys: MutableSet<K>
    override val values: MutableCollection<V>
    override val size: Int

    override fun containsKey(key: K): Boolean

    override fun isEmpty(): Boolean

    override fun containsValue(value: V): Boolean

    override fun get(key: K): V?
}
