package matt.collect.weak.soft

import matt.collect.dmap.DefaultStoringMap
import matt.collect.dmap.withStoringDefault
import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference
import java.util.AbstractMap
import kotlin.collections.MutableMap.MutableEntry

fun <K: Any, V: Any> lazySoftMap(op: (K)->V): DefaultStoringMap<K, V> = SoftHashMap<K, V>().withStoringDefault(op)

class SoftHashMap<K: Any, V: Any>: AbstractMap<K, V>() {
  private val hash: MutableMap<K, SoftReference<V>> = HashMap()
  private val queue: ReferenceQueue<V> = ReferenceQueue<V>()

  @Synchronized
  override fun get(key: K): V? {
	var res: V? = null
	val sr = hash[key]
	if (sr != null) {
	  res = sr.get()
	  if (res == null) hash.remove(key)
	}
	return res
  }

  @Synchronized
  private fun processQueue() {
	while (true) {
	  val sv = queue.poll()
	  if (sv != null) hash.remove(hash.entries.firstOrNull { it.value == sv }?.key) else return
	}
  }

  @Synchronized
  override fun put(key: K, value: V): V? {
	processQueue()
	return hash.put(key, SoftReference(value, queue))?.get()
  }

  @Synchronized
  override fun remove(key: K): V? {
	processQueue()
	return hash.remove(key)?.get()
  }

  @Synchronized
  override fun clear() {
	processQueue()
	hash.clear()
  }


  override val entries: MutableSet<MutableEntry<K, V>>
	@Synchronized get() = TODO("Figure this out")


  override val size: Int
	@Synchronized get() = hash.size
}