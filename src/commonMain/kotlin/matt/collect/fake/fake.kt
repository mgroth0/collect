package matt.collect.fake

import matt.collect.itr.toFakeMutableIterator
import matt.lang.ILLEGAL

fun <E> List<E>.toFakeMutableList() = FakeMutableList(this)

class FakeMutableList<E>(private val list: List<E>): List<E> by list, MutableList<E> {
  override fun add(element: E) = ILLEGAL
  override fun add(index: Int, element: E) = ILLEGAL
  override fun addAll(index: Int, elements: Collection<E>) = ILLEGAL
  override fun addAll(elements: Collection<E>) = ILLEGAL
  override fun clear() = ILLEGAL
  override fun removeAt(index: Int) = ILLEGAL
  override fun set(index: Int, element: E) = ILLEGAL
  override fun retainAll(elements: Collection<E>) = ILLEGAL
  override fun removeAll(elements: Collection<E>) = ILLEGAL
  override fun remove(element: E) = ILLEGAL
  override fun iterator() = list.iterator().toFakeMutableIterator()
  override fun listIterator() = ILLEGAL
  override fun listIterator(index: Int) = ILLEGAL
  override fun subList(fromIndex: Int, toIndex: Int) = ILLEGAL
}