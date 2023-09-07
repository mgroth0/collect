package matt.collect.fake

import matt.collect.itr.toFakeMutableIterator
import matt.collect.itr.toFakeMutableListIterator
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
  override fun listIterator() = list.listIterator().toFakeMutableListIterator()
  override fun listIterator(index: Int) = list.listIterator(index).toFakeMutableListIterator()
  override fun subList(fromIndex: Int, toIndex: Int) = FakeMutableList(list.subList(fromIndex,toIndex))
}


class FakeList<E>: List<E> {
  override val size: Int
    get() = error("this is fake")

  override fun contains(element: E): Boolean {
    error("this is fake")
  }

  override fun containsAll(elements: Collection<E>): Boolean {
    error("this is fake")
  }

  override fun get(index: Int): E {
    error("this is fake")
  }

  override fun indexOf(element: E): Int {
    error("this is fake")
  }

  override fun isEmpty(): Boolean {
    error("this is fake")
  }

  override fun iterator(): Iterator<E> {
    error("this is fake")
  }

  override fun lastIndexOf(element: E): Int {
    error("this is fake")
  }

  override fun listIterator(): ListIterator<E> {
    error("this is fake")
  }

  override fun listIterator(index: Int): ListIterator<E> {
    error("this is fake")
  }

  override fun subList(fromIndex: Int, toIndex: Int): List<E> {
    error("this is fake")
  }
}