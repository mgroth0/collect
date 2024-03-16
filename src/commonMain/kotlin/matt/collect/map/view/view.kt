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


class MapView<K, V : Any, VV>(
    private val map: Map<K, V>,
    private val view: (V) -> VV?
) : Map<K, VV> {
    override val entries: Set<Entry<K, VV>>
        get() = TODO()
    override val keys: Set<K>
        get() = TODO()
    override val size: Int
        get() = TODO()
    override val values: Collection<VV>
        get() = TODO()

    override fun isEmpty(): Boolean {
        TODO()
    }

    override fun get(key: K): VV? {
        TODO()
    }

    override fun containsValue(value: VV): Boolean {
        TODO()
    }

    override fun containsKey(key: K): Boolean {
        TODO()
    }
}
