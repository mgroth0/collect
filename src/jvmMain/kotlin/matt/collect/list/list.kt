package matt.collect.list

fun <E> MutableList<E>.replaceEvery(a: E, b: E) = replaceAll {
  when (it) {
	a    -> b
	else -> it
  }
}

fun <E> MutableList<E>.removeOrAdd(e: E) {
  val removed = remove(e)
  if (!removed) add(e)
}

fun <E> MutableList<E>.removeOrAdd(e: E, index: Int) {
  val removed = remove(e)
  if (!removed) add(index, e)
}