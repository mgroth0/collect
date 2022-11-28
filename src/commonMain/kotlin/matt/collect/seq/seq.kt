package matt.collect.seq

import matt.lang.whileTrue

fun <E> sequenceUntil(endExclusive: E, op: ()->E) = sequence {
  whileTrue {
	val e = op()
	if (e != endExclusive) yield(e)
	e != endExclusive
  }
}

fun <E> Sequence<E>.interleave(sep: E) = sequence {
  val itr = this@interleave.iterator()
  while (itr.hasNext()) {
	println("interleave 1")
	yield(itr.next())
	println("interleave 2")
	if (itr.hasNext()) {
	  println("interleave 3")
	  yield(sep)
	  println("interleave 4")
	}
	println("interleave 5")
  }
}

fun <E> Sequence<E>.skip(num: Int) = sequence<E> {
  require(num >= 0)
  val itr = this@skip.iterator()
  var numLeft = num
  while (numLeft > 0) {
	if (!itr.hasNext()) return@sequence
	itr.next()
	numLeft--
  }
  yieldAll(itr)
}