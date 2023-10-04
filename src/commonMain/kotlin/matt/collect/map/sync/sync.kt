package matt.collect.map.sync

import matt.lang.anno.OnlySynchronizedOnJvm
import kotlin.collections.Map.Entry
import kotlin.collections.MutableMap.MutableEntry

fun <K, V> Map<K, V>.synchronized() = SynchronizedMap(this)


open class SynchronizedMap<K, V>(protected open val map: Map<K, V>) : Map<K, V> {
    override val entries: Set<Entry<K, V>> @OnlySynchronizedOnJvm get() = map.entries
    override val keys: Set<K> @OnlySynchronizedOnJvm get() = map.keys
    override val size: Int @OnlySynchronizedOnJvm get() = map.size
    override val values: Collection<V> @OnlySynchronizedOnJvm get() = map.values

    @OnlySynchronizedOnJvm
    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    @OnlySynchronizedOnJvm
    override fun get(key: K): V? {
        return map[key]
    }

    @OnlySynchronizedOnJvm
    override fun containsValue(value: V): Boolean {
        return map.containsValue(value)
    }

    @OnlySynchronizedOnJvm
    override fun containsKey(key: K): Boolean {
        return map.containsKey(key)
    }

}

fun <K, V> MutableMap<K, V>.synchronized(): MutableMap<K, V> = SynchronizedMutableMap(this)


class SynchronizedMutableMap<K, V>(protected override val map: MutableMap<K, V>) : SynchronizedMap<K, V>(map),
    MutableMap<K, V> {

    override val entries: MutableSet<MutableEntry<K, V>>
        @OnlySynchronizedOnJvm get() = map.entries
    override val keys: MutableSet<K>
        @OnlySynchronizedOnJvm get() = map.keys
    override val values: MutableCollection<V>
        @OnlySynchronizedOnJvm get() = map.values

    @OnlySynchronizedOnJvm
    override fun clear() {
        return map.clear()
    }

    @OnlySynchronizedOnJvm
    override fun put(
        key: K,
        value: V
    ): V? {
        return map.put(key, value)
    }


    @OnlySynchronizedOnJvm
    override fun putAll(from: Map<out K, V>) {
        return map.putAll(from)
    }


    @OnlySynchronizedOnJvm
    override fun remove(key: K): V? {
        return map.remove(key)
    }

    @OnlySynchronizedOnJvm
    fun getOrPutAtomically(
        key: K,
        defaultValue: () -> V
    ): V {
        return map.getOrPut(key, defaultValue)
    }

}