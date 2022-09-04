package matt.collect.maxlist

import matt.collect.fake.toFakeMutableList
import matt.collect.itr.ItrChange.Add
import matt.collect.itr.MutableListIteratorWrapper
import matt.lang.err


class MaxList<E>(val maxsize: Int): MutableList<E> {

  private val list = mutableListOf<E>()

  fun check() {
	if (size > maxsize) err("size of $size exceeds maxsize ($maxsize) for $this")
  }

  fun ensureSpaceFor(xMore: Int) {
	if (size > maxsize - xMore) err("size of $size is not enough space for $xMore more, maxsize ($maxsize), for $this")
  }

  override val size get() = list.size

  override fun clear() {
	list.clear()
  }

  override fun addAll(elements: Collection<E>): Boolean {
	ensureSpaceFor(elements.size)
	return list.addAll(elements)
  }

  override fun addAll(index: Int, elements: Collection<E>): Boolean {
	ensureSpaceFor(elements.size)
	return list.addAll(index, elements)
  }

  override fun add(index: Int, element: E) {
	ensureSpaceFor(1)
	list.add(index, element)
  }

  override fun add(element: E): Boolean {
	ensureSpaceFor(1)
	return list.add(element)
  }

  override fun get(index: Int) = list.get(index)

  override fun isEmpty() = list.isEmpty()

  override fun iterator(): MutableIterator<E> = listIterator()

  override fun listIterator() = listIterator(0)

  override fun listIterator(index: Int) = MutableListIteratorWrapper(list, index = index, changeWrapper = { c, op ->
	if (c == Add) {
	  ensureSpaceFor(1)
	}
	op()
  })


  override fun removeAt(index: Int) = list.removeAt(index)

  override fun subList(fromIndex: Int, toIndex: Int) = list.subList(fromIndex, toIndex).toFakeMutableList()

  override fun set(index: Int, element: E) = list.set(index, element)

  override fun retainAll(elements: Collection<E>) = list.retainAll(elements)

  override fun removeAll(elements: Collection<E>) = list.removeAll(elements)

  override fun remove(element: E) = list.remove(element)

  override fun lastIndexOf(element: E) = list.lastIndexOf(element)

  override fun indexOf(element: E) = list.indexOf(element)

  override fun containsAll(elements: Collection<E>) = list.containsAll(elements)

  override fun contains(element: E) = list.contains(element)

}