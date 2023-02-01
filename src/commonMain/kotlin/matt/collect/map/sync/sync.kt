package matt.collect.map.sync

import kotlin.collections.Map.Entry
import kotlin.collections.MutableMap.MutableEntry
import kotlin.jvm.Synchronized

fun <K, V> Map<K, V>.synchronized() = SynchronizedMap(this)


open class SynchronizedMap<K, V>(protected open val map: Map<K, V>): Map<K, V> {
  override val entries: Set<Entry<K, V>> @Synchronized get() = map.entries
  override val keys: Set<K> @Synchronized get() = map.keys
  override val size: Int @Synchronized get() = map.size
  override val values: Collection<V> @Synchronized get() = map.values

  @Synchronized
  override fun isEmpty(): Boolean {
	return map.isEmpty()
  }

  @Synchronized
  override fun get(key: K): V? {
	return map[key]
  }

  @Synchronized
  override fun containsValue(value: V): Boolean {
	return map.containsValue(value)
  }

  @Synchronized
  override fun containsKey(key: K): Boolean {
	return map.containsKey(key)
  }

}

fun <K,V> MutableMap<K,V>.synchronized() = SynchronizedMutableMap(this)


class SynchronizedMutableMap<K, V>(protected override val map: MutableMap<K, V>): SynchronizedMap<K, V>(map),
																				  MutableMap<K, V> {

  override val entries: MutableSet<MutableEntry<K, V>>
	@Synchronized get() = map.entries
  override val keys: MutableSet<K>
	@Synchronized get() = map.keys
  override val values: MutableCollection<V>
	@Synchronized get() = map.values

  @Synchronized
  override fun clear() {
	return map.clear()
  }

  @Synchronized
  override fun put(key: K, value: V): V? {
	return map.put(key, value)
  }


  @Synchronized
  override fun putAll(from: Map<out K, V>) {
	return map.putAll(from)
  }


  @Synchronized
  override fun remove(key: K): V? {
	return map.remove(key)
  }
}