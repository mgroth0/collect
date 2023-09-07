package matt.collect.list.sub

import matt.collect.list.StructuralList

class SubList<E>(): StructuralList<E>(), List<E> {
  override val size: Int
	get() = TODO("Not yet implemented")

  override fun contains(element: E): Boolean {
	TODO("Not yet implemented")
  }

  override fun containsAll(elements: Collection<E>): Boolean {
	TODO("Not yet implemented")
  }

  override fun get(index: Int): E {
	TODO("Not yet implemented")
  }

  override fun indexOf(element: E): Int {
	TODO("Not yet implemented")
  }

  override fun isEmpty(): Boolean {
	TODO("Not yet implemented")
  }

  override fun iterator(): Iterator<E> {
	TODO("Not yet implemented")
  }

  override fun lastIndexOf(element: E): Int {
	TODO("Not yet implemented")
  }

  override fun listIterator(): ListIterator<E> {
	TODO("Not yet implemented")
  }

  override fun listIterator(index: Int): ListIterator<E> {
	TODO("Not yet implemented")
  }

  override fun subList(fromIndex: Int, toIndex: Int): List<E> {
	TODO("Not yet implemented")
  }
}