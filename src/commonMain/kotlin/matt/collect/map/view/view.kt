package matt.collect.map.view

import kotlin.collections.Map.Entry

class FilteredMap<K, V : Any>(
    private val map: Map<K, V>,
    private val valueFilter: (V) -> Boolean
) : Map<K, V> {
    override val entries: Set<Entry<K, V>>
        get() = TODO()
    override val keys: Set<K>
        get() = TODO()
    override val size: Int
        get() = TODO()
    override val values: Collection<V>
        get() = TODO()

    override fun isEmpty(): Boolean {
        TODO()
    }

    override fun get(key: K): V? {
        TODO()
    }

    override fun containsValue(value: V): Boolean {
        TODO()
    }

    override fun containsKey(key: K): Boolean {
        TODO()
    }
}
