package matt.collect.list

fun <E> MutableList<E>.replaceEvery(a: E, b: E) = replaceAll {
  when (it) {
	a    -> b
	else -> it
  }
}