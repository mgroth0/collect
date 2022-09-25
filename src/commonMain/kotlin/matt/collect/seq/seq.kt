@file:OptIn(ExperimentalContracts::class)

package matt.collect.seq

import matt.lang.whileTrue
import kotlin.contracts.ExperimentalContracts

@OptIn(ExperimentalContracts::class)
fun <E> sequenceUntil(endExclusive: E, op: ()->E) = sequence {
  whileTrue {
	val e = op()
	if (e != endExclusive) yield(e)
	e != endExclusive
  }
}