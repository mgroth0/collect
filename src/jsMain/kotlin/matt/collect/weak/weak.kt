package matt.collect.weak

import matt.lang.dyn.call
import kotlin.collections.MutableMap.MutableEntry

actual class WeakMap<K, V> : MutableMap<K, V> {
    private val map = call("WeakMap")

    actual override val entries: MutableSet<MutableEntry<K, V>>
        get() = TODO()
    actual override val keys: MutableSet<K>
        get() = TODO()
    actual override val size: Int
        get() = TODO()
    actual override val values: MutableCollection<V>
        get() = TODO()

    actual override fun clear() {
        TODO()
    }

    actual override fun isEmpty(): Boolean {
        TODO()
    }

    actual override fun remove(key: K): V? {
        TODO()
    }

    actual override fun putAll(from: Map<out K, V>) {
        TODO()
    }

    actual override fun put(
        key: K,
        value: V,
    ): V? {
        val oldValue = map[key.hashCode()]
        map[key.hashCode()] = value
        return oldValue as V?
    }

    actual override fun get(key: K): V? {
// 	if (verbose) println("looking at weakmap with key:${key}")
        val r = map[key.hashCode()] as V?
// 	if (verbose) println("r=$r, r==null=${r == null}")
        return r
    }

    actual override fun containsValue(value: V): Boolean {
        TODO()
    }

    actual override fun containsKey(key: K): Boolean {
        TODO()
    }
}
