package matt.collect.dmap

fun <K, V> MutableMap<K, V>.withStoringDefault(
  d: (K)->V
): DefaultStoringMap<K, V> {
  return DefaultStoringMap(this, d)
}

interface CanBeNotNullMutableMap<K, V>: MutableMap<K, V>, CanBeNotNullMap<K, V>

interface CanBeNotNullMap<K, V>: Map<K, V> {
  override operator fun get(key: K): V
}

class DefaultStoringMap<K, V>(
  val map: MutableMap<K, V>,
  val d: (K)->V
): CanBeNotNullMutableMap<K, V> {
  override val size: Int
	get() = map.size

  override fun containsKey(key: K) = map.containsKey(key)

  override fun containsValue(value: V) = map.containsValue(value)

  override operator fun get(key: K): V {
	return map[key] ?: d(key).also {
	  map[key] = it
	}
  }

  fun getWithoutSetting(key: K): V? {
	return map[key]
  }

  override fun isEmpty() = map.isEmpty()

  override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
	get() = map.entries
  override val keys: MutableSet<K>
	get() = map.keys
  override val values: MutableCollection<V>
	get() = map.values

  override fun clear() = map.clear()

  override fun put(key: K, value: V): V? = map.put(key, value)

  override fun putAll(from: Map<out K, V>) = map.putAll(from)

  override fun remove(key: K): V? = map.remove(key)

}

