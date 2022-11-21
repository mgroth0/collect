package matt.collect.seq

import matt.lang.whileTrue

fun <E> sequenceUntil(endExclusive: E, op: ()->E) = sequence {
  whileTrue {
	val e = op()
	if (e != endExclusive) yield(e)
	e != endExclusive
  }
}

fun <E> Sequence<E>.interleave(sep: E) = sequence<E> {
  val itr = this@interleave.iterator()
  while (itr.hasNext()) {
	yield(itr.next())
	if (itr.hasNext()) yield(sep)
  }
}