package matt.collect.list.recentlist

import matt.collect.itr.FakeMutableIterator
import java.util.LinkedList
import java.util.Queue

class EvitctingQueue<E: Any>(val capacity: Int): Queue<E> {


  private val data = LinkedList<E>()

  @Synchronized
  override fun add(element: E): Boolean {
    data.add(element)
    if (data.size > capacity) data.pop()
    return true
  }

  override fun addAll(elements: Collection<E>): Boolean {
    TODO("Not yet implemented")
  }

  override fun clear() {
    TODO("Not yet implemented")
  }

  override fun iterator() =  FakeMutableIterator<E>(data.iterator())

  override fun remove(): E {
    TODO("Not yet implemented")
  }

  override fun retainAll(elements: Collection<E>): Boolean {
    TODO("Not yet implemented")
  }

  override fun removeAll(elements: Collection<E>): Boolean {
    TODO("Not yet implemented")
  }

  override fun remove(element: E): Boolean {
    TODO("Not yet implemented")
  }

  override fun isEmpty(): Boolean {
    TODO("Not yet implemented")
  }

  @Synchronized
  override fun poll() = data.poll()

  override fun element(): E {
    TODO("Not yet implemented")
  }

  @Synchronized
  override fun peek() = data.peek()

  override fun offer(e: E): Boolean {
    TODO("Not yet implemented")
  }

  override fun containsAll(elements: Collection<E>): Boolean {
    TODO("Not yet implemented")
  }

  override fun contains(element: E): Boolean {
    TODO("Not yet implemented")
  }

  override val size: Int get() = data.size


}