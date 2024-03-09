package matt.collect.map

import matt.collect.itr.FakeMutableIterator
import matt.collect.map.dmap.inter.CanBeNotNullMap
import matt.collect.map.dmap.inter.CanBeNotNullMutableMap
import matt.collect.map.dmap.inter.withStoringDefault
import matt.lang.anno.Open
import matt.lang.common.err
import matt.prim.str.lower
import kotlin.collections.MutableMap.MutableEntry


fun <K, V : Any> lazyMap(getter: (K) -> V): CanBeNotNullMap<K, V> = mutableMapOf<K, V>().withStoringDefault(getter)

fun <K, V : Any> lazyMutableMap(getter: (K) -> V): CanBeNotNullMutableMap<K, V> = mutableMapOf<K, V>().withStoringDefault(getter)


class MapFromKeyValueLists<K, V>(
    private val keyList: MutableList<K>,
    private val valueList: MutableList<V>
) : MutableMap<K, V> {

    private val mapSnap: Map<K, V> get() = keyList.zip(valueList).toMap()

    override val entries: MutableSet<MutableEntry<K, V>> get() = FakeMutableSet(mapSnap.toMutableMap().entries)
    override val keys: MutableSet<K> get() = FakeMutableSet(mapSnap.keys.toMutableSet())
    override val size: Int get() = mapSnap.size
    override val values: MutableCollection<V> get() = FakeMutableSet(mapSnap.values.toMutableSet())

    override fun clear() {
        keyList.clear()
        valueList.clear()
    }

    override fun isEmpty() = mapSnap.isEmpty()

    override fun remove(key: K): V? {
        val index = keyList.indexOf(key)
        return if (index >= 0) {
            keyList.removeAt(index)
            return valueList.removeAt(index)
        } else null
    }

    override fun putAll(from: Map<out K, V>) {
        from.forEach { (k, v) ->
            put(
                k,
                v
            )
        }
    }

    override fun put(
        key: K,
        value: V
    ): V? {
        val index = keyList.indexOf(key)
        return if (index >= 0) {
            valueList.add(
                index,
                value
            )
            return valueList.removeAt(index + 1)
        } else {
            keyList.add(key)
            valueList.add(value)
            null
        }
    }

    override fun get(key: K): V? = mapSnap[key]

    override fun containsValue(value: V) = mapSnap.containsValue(value)

    override fun containsKey(key: K) = mapSnap.containsKey(key)
}


sealed class CaseInsensitiveMap<V> : Map<String, V> {
    protected val map = mutableMapOf<String, V>()

    @Open
    override val entries: Set<Map.Entry<String, V>>
        get() = map.entries

    @Open
    override val keys: Set<String>
        get() = map.keys
    final override val size: Int
        get() = map.size

    @Open
    override val values: Collection<V>
        get() = map.values

    final override fun containsKey(key: String): Boolean = map.containsKey(key.lower())

    final override fun containsValue(value: V): Boolean = map.containsValue(value)

    final override fun get(key: String): V? = map[key.lower()]

    final override fun isEmpty(): Boolean = map.isEmpty()
}

fun <E> Set<E>.toFakeMutableSet() = FakeMutableSet(this)

class FakeMutableSet<E>(val set: Collection<E>) : MutableSet<E> {


    override fun add(element: E): Boolean {
        err("tried to add in ${FakeMutableSet::class.simpleName}")
    }

    override fun addAll(elements: Collection<E>): Boolean {
        err("tried to addAll in ${FakeMutableSet::class.simpleName}")
    }

    override fun clear() {
        err("tried to clear in ${FakeMutableSet::class.simpleName}")
    }

    override fun iterator(): MutableIterator<E> = FakeMutableIterator(set.iterator())

    override fun remove(element: E): Boolean {
        err("tried to remove in ${FakeMutableSet::class.simpleName}")
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        err("tried to removeAll in ${FakeMutableSet::class.simpleName}")
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        err("tried to retainAll in ${FakeMutableSet::class.simpleName}")
    }

    override val size: Int
        get() = set.size

    override fun contains(element: E): Boolean = set.contains(element)

    override fun containsAll(elements: Collection<E>): Boolean = set.containsAll(elements)

    override fun isEmpty(): Boolean = set.isEmpty()
}


class MutableCaseInsensitiveMap<V> : CaseInsensitiveMap<V>(), MutableMap<String, V> {
    override val entries: MutableSet<MutableMap.MutableEntry<String, V>>
        get() = FakeMutableSet(map.entries)
    override val keys: MutableSet<String>
        get() = FakeMutableSet(map.keys)
    override val values: MutableCollection<V>
        get() = FakeMutableSet(map.values)

    override fun clear() {
        map.clear()
    }

    override fun put(
        key: String,
        value: V
    ): V? =
        map.put(
            key.lower(),
            value
        )

    override fun putAll(from: Map<out String, V>) {
        map.putAll(from.mapKeys { it.key.lower() })
    }

    override fun remove(key: String): V? = map.remove(key.lower())
}


fun Map<*, *>.toDictString(
    multiLine: Boolean = true
): String {
    val newline = if (multiLine) '\n' else ' '
    return entries.joinToString(
        prefix = "{$newline",
        postfix = "$newline}",
        separator = ",$newline"
    ) { "${it.key}: ${it.value}" }
}


fun <K, V> Map<K, V>.readOnly() = ReadOnlyMap(this)
class ReadOnlyMap<K, V>(map: Map<K, V>) : Map<K, V> by map

fun <K, V> Map<K, V>.filterOutNullKeys(): Map<K & Any, V> {
    val r = mutableMapOf<K & Any, V>()
    entries.forEach {
        val k = it.key
        if (k != null) {
            r[k] = it.value
        }
    }
    return r
}

fun <K, V> Map<K, V>.filterOutNullValues(): Map<K, V & Any> {
    val r = mutableMapOf<K, V & Any>()
    entries.forEach {
        val v = it.value
        if (v != null) {
            r[it.key] = v
        }
    }
    return r
}



class MapThatRemovesOnGet<K, V: Any>(
    private val map: MutableMap<K, V>
): MutableMap<K, V> {
    override val size: Int
        get() = map.size

    override fun containsKey(key: K): Boolean = map.containsKey(key)

    override fun containsValue(value: V): Boolean = map.containsValue(value)

    override fun get(key: K): V? = remove(key)

    override fun isEmpty(): Boolean = map.isEmpty()

    override val entries: MutableSet<MutableEntry<K, V>>
        get() =
            object: MutableSet<MutableEntry<K, V>> {
                override fun add(element: MutableEntry<K, V>): Boolean {
                    TODO("Not yet implemented")
                }

                override fun addAll(elements: Collection<MutableEntry<K, V>>): Boolean {
                    TODO("Not yet implemented")
                }

                override val size: Int
                    get() = TODO("Not yet implemented")

                override fun clear() {
                    TODO("Not yet implemented")
                }

                override fun isEmpty(): Boolean {
                    TODO("Not yet implemented")
                }

                override fun containsAll(elements: Collection<MutableEntry<K, V>>): Boolean {
                    TODO("Not yet implemented")
                }

                override fun contains(element: MutableEntry<K, V>): Boolean {
                    TODO("Not yet implemented")
                }

                override fun iterator(): MutableIterator<MutableEntry<K, V>> {
                    TODO("Not yet implemented")
                }

                override fun retainAll(elements: Collection<MutableEntry<K, V>>): Boolean {
                    TODO("Not yet implemented")
                }

                override fun removeAll(elements: Collection<MutableEntry<K, V>>): Boolean {
                    TODO("Not yet implemented")
                }

                override fun remove(element: MutableEntry<K, V>): Boolean {
                    TODO("Not yet implemented")
                }
            }
    override val keys: MutableSet<K>
        get() = map.keys
    override val values: MutableCollection<V>
        get() = map.values

    override fun clear() {
        map.clear()
    }

    override fun put(
        key: K,
        value: V
    ): V? = map.put(key, value)

    override fun putAll(from: Map<out K, V>) {
        map.putAll(from)
    }

    override fun remove(key: K): V? = map.remove(key)
}
