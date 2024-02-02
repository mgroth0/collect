package matt.collect.map.sync

import matt.lang.anno.Open
import matt.lang.sync.ReferenceMonitor
import matt.lang.sync.inSync
import kotlin.collections.Map.Entry
import kotlin.collections.MutableMap.MutableEntry

fun <K, V> Map<K, V>.synchronized() = SynchronizedMap(this)

open class SynchronizedMap<K, V>(protected open val map: Map<K, V>) : Map<K, V>, ReferenceMonitor {
    @Open
    override val entries: Set<Entry<K, V>> get() = inSync { map.entries }

    @Open
    override val keys: Set<K> get() = inSync { map.keys }
    final override val size: Int get() = inSync { map.size }

    @Open
    override val values: Collection<V> get() = inSync { map.values }

    final override fun isEmpty(): Boolean =
        inSync {
            return map.isEmpty()
        }

    final override fun get(key: K): V? =
        inSync {
            return map[key]
        }

    final override fun containsValue(value: V): Boolean =
        inSync {
            return map.containsValue(value)
        }

    final override fun containsKey(key: K): Boolean =
        inSync {
            return map.containsKey(key)
        }
}

fun <K, V> MutableMap<K, V>.synchronized(): MutableMap<K, V> = SynchronizedMutableMap(this)

class SynchronizedMutableMap<K, V>(protected override val map: MutableMap<K, V>) :
    SynchronizedMap<K, V>(map),
    MutableMap<K, V> {
    override val entries: MutableSet<MutableEntry<K, V>>
        get() = inSync { map.entries }
    override val keys: MutableSet<K>
        get() = inSync { map.keys }
    override val values: MutableCollection<V>
        get() = inSync { map.values }

    override fun clear() =
        inSync {
            map.clear()
        }

    override fun put(
        key: K,
        value: V,
    ): V? =
        inSync {
            return map.put(key, value)
        }

    override fun putAll(from: Map<out K, V>) =
        inSync {
            map.putAll(from)
        }

    override fun remove(key: K): V? =
        inSync {
            return map.remove(key)
        }

    fun getOrPutAtomically(
        key: K,
        defaultValue: () -> V,
    ): V =
        inSync {
            return map.getOrPut(key, defaultValue)
        }
}
