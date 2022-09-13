package matt.collect

import matt.lang.NOT_IMPLEMENTED


infix fun <K, V> Map<K, V>.isEquivalentTo(other: Map<K, V>?): Boolean {
  if (other == null) return false
  if (this.keys.size != other.keys.size) return false
  this.forEach { (k, v) ->
	if (k !in other) return false
	if (v != other[k]) return false
  }
  return true
}


fun <E> Collection<E>.snapshotToPreventConcurrentModification() = when (this) {
  is List -> toList()
  else    -> NOT_IMPLEMENTED
}
