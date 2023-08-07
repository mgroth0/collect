package matt.collect.map.dmap

import java.util.concurrent.Semaphore
import kotlin.contracts.InvocationKind.EXACTLY_ONCE
import kotlin.contracts.contract


actual class DefaultStoringMap<K, V> actual constructor(
    actual val map: MutableMap<K, V>,
    actual val d: (K) -> V
) : CanBeNotNullMutableMap<K, V> {
    actual override val size: Int
        get() = map.size

    actual override fun containsKey(key: K) = map.containsKey(key)

    actual override fun containsValue(value: V) = map.containsValue(value)

    private val multiMonitor = MultiMonitor<K>()

    actual override operator fun get(key: K): V {

        return multiMonitor.with(key) {
            map.getOrPut(key) { d(key) }
        }
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
        value: V
    ): V? = map.put(key, value)

    actual override fun putAll(from: Map<out K, V>) = map.putAll(from)

    actual override fun remove(key: K): V? = map.remove(key)

}


class MultiMonitor<T> {
    fun <R> with(
        t: T,
        op: () -> R
    ): R {
        contract {
            callsInPlace(op, EXACTLY_ONCE)
        }
        val sem = takeMonitor(t)
        sem.acquire()
        val r = op()
        sem.release()
        giveBackMonitor(t)
        return r
    }


    private val currentlyHeld = mutableMapOf<T, Semaphore>()
    private val currentlyWaiting = mutableMapOf<T, Int>()

    @Synchronized
    private fun takeMonitor(t: T): Semaphore {

        val r = currentlyHeld.getOrPut(t) { Semaphore(1) }

        val c = currentlyWaiting[t] ?: 0
        currentlyWaiting[t] = c + 1
        return r
    }

    @Synchronized
    private fun giveBackMonitor(t: T) {
        val c = currentlyWaiting[t]!!
        val newC = c - 1
        currentlyWaiting[t] = newC
        if (newC == 0) {
            currentlyWaiting.remove(t)
            currentlyHeld.remove(t)
        }
    }

}