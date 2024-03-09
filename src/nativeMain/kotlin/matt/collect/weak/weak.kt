package matt.collect.weak

import kotlin.collections.MutableMap.MutableEntry

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class WeakMap<K, V> actual constructor() : MutableMap<K, V> {
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
        value: V
    ): V? {
        TODO()
    }

    actual override fun get(key: K): V? {
        TODO()
    }

    actual override fun containsValue(value: V): Boolean {
        TODO()
    }

    actual override fun containsKey(key: K): Boolean {
        TODO()
    }
}
