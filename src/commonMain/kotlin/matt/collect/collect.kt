package matt.collect

import matt.lang.NOT_IMPLEMENTED
import matt.lang.function.Convert


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


inline fun <E, R> Iterable<E>.mapToSet(transform: Convert<E, R>) = mapTo(mutableSetOf(), transform)
fun <E, R> Array<E>.mapToSet(transform: Convert<E, R>) = mapTo(mutableSetOf(), transform)
fun <E, R> Sequence<E>.mapToSet(transform: Convert<E, R>) = mapTo(mutableSetOf(), transform)