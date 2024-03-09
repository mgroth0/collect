
package matt.collect.map.dmap

import matt.collect.map.dmap.inter.CanBeNotNullMutableMap
import java.util.concurrent.CountDownLatch

actual class DefaultStoringMap<K, V : Any> actual constructor(
    actual val map: MutableMap<K, V>,
    actual val d: (K) -> V
) : CanBeNotNullMutableMap<K, V> {
    actual override val size: Int
        get() = map.size

    @Synchronized
    actual override fun containsKey(key: K) = map.containsKey(key)

    @Synchronized
    actual override fun containsValue(value: V) = map.containsValue(value)

    private inner class CreateJob(val key: K) {
        var result: V? = null
        val latch = CountDownLatch(1)
    }

    private val createJobs = mutableMapOf<K, CreateJob>()

    actual override operator fun get(key: K): V {
        val g =
            synchronized(this) {
                map[key]
            }
        if (g != null) return g
        var r: V?
        do {
            var jobIsMine = false
            val createJob =
                synchronized(this) {
                    map[key]?.let { return it }
                    createJobs.getOrPut(key) {
                        jobIsMine = true
                        CreateJob(key)
                    }
                }
            if (jobIsMine) {
                try {
                    r = d(key)
                    synchronized(this) {
                        map[key] = r!!
                    }
                    createJob.result = r
                } finally {
                    synchronized(this) {
                        createJobs.remove(key)
                    }
                    createJob.latch.countDown()
                }
            } else {
                createJob.latch.await()
                r = createJob.result
                r ?: get(key)
            }
        } while (r == null)
        return r
    }

    @Synchronized
    actual fun getWithoutSetting(key: K): V? = map[key]

    @Synchronized
    actual override fun isEmpty() = map.isEmpty()

    /* these all need to be more deephys synchronized... this is a common issue */
    actual override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        @Synchronized get() = map.entries

    /* these all need to be more deephys synchronized... this is a common issue */
    actual override val keys: MutableSet<K>
        @Synchronized get() = map.keys

    /* these all need to be more deephys synchronized... this is a common issue */
    actual override val values: MutableCollection<V>
        @Synchronized get() = map.values

    @Synchronized
    actual override fun clear() = map.clear()

    @Synchronized
    actual override fun put(
        key: K,
        value: V
    ): V? = map.put(key, value)

    @Synchronized
    actual override fun putAll(from: Map<out K, V>) = map.putAll(from)

    @Synchronized
    actual override fun remove(key: K): V? = map.remove(key)
}
