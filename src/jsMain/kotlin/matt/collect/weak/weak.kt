package matt.collect.weak

import matt.lang.dyn.call
import kotlin.collections.MutableMap.MutableEntry

actual class WeakMap<K, V>: MutableMap<K, V> {

  private val map = call("WeakMap")


  override val entries: MutableSet<MutableEntry<K, V>>
	get() = TODO("Not yet implemented")
  override val keys: MutableSet<K>
	get() = TODO("Not yet implemented")
  override val size: Int
	get() = TODO("Not yet implemented")
  override val values: MutableCollection<V>
	get() = TODO("Not yet implemented")

  override fun clear() {
	TODO("Not yet implemented")
  }

  override fun isEmpty(): Boolean {
	TODO("Not yet implemented")
  }

  override fun remove(key: K): V? {
	TODO("Not yet implemented")
  }

  override fun putAll(from: Map<out K, V>) {
	TODO("Not yet implemented")
  }

  override fun put(key: K, value: V): V? {
	val oldValue = map[key.hashCode()]
	map[key.hashCode()] = value
	return oldValue as V?
  }

  override fun get(key: K): V? {
//	if (verbose) println("looking at weakmap with key:${key}")
	val r = map[key.hashCode()] as V?
//	if (verbose) println("r=$r, r==null=${r == null}")
	return r
  }

  override fun containsValue(value: V): Boolean {
	TODO("Not yet implemented")
  }

  override fun containsKey(key: K): Boolean {
	TODO("Not yet implemented")
  }

}
