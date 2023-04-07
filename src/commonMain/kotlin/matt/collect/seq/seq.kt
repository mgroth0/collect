package matt.collect.seq

import matt.lang.function.Convert
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
	yield(itr.next())
	if (itr.hasNext()) {
	  yield(sep)
	}
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

inline fun <E, R> Sequence<E>.skipDuplicatesBy(crossinline transform: Convert<E, R>) = sequence<E> {
  val itr = this@skipDuplicatesBy.iterator()
  val yielded = mutableSetOf<R>()
  var n: E
  while (itr.hasNext()) {
	n = itr.next()
	val transformed = transform(n)
	if (transformed !in yielded) {
	  yield(n)
	  yielded += transformed
	}
  }
}

fun <E> Sequence<E>.skipDuplicates() = skipDuplicatesBy { it }


