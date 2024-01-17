package matt.collect.privmutlist

import matt.collect.list.StructuralList

abstract class ProtectedMutableList<E>(private val list: MutableList<E> = mutableListOf()): StructuralList<E>(), List<E> by list {

  protected fun add(e: E) = list.add(e)

  protected operator fun plusAssign(e: E) {
	add(e)
  }

}