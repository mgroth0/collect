package matt.collect.fake

import matt.collect.itr.toFakeMutableIterator
import matt.lang.err

fun <E> List<E>.toFakeMutableList() = FakeMutableList(this)

class FakeMutableList<E>(private val list: List<E>): List<E> by list, MutableList<E> {
  private fun theErr(): Nothing = err("$this is not really mutable")
  override fun add(element: E) = theErr()
  override fun add(index: Int, element: E) = theErr()
  override fun addAll(index: Int, elements: Collection<E>) = theErr()
  override fun addAll(elements: Collection<E>) = theErr()
  override fun clear() = theErr()
  override fun removeAt(index: Int) = theErr()
  override fun set(index: Int, element: E) = theErr()
  override fun retainAll(elements: Collection<E>) = theErr()
  override fun removeAll(elements: Collection<E>) = theErr()
  override fun remove(element: E) = theErr()
  override fun iterator() = list.iterator().toFakeMutableIterator()
  override fun listIterator() = theErr()
  override fun listIterator(index: Int) = theErr()
  override fun subList(fromIndex: Int, toIndex: Int) = theErr()
}