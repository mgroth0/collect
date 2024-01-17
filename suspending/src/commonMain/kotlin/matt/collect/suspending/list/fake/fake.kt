package matt.collect.suspending.list.fake

import matt.collect.suspending.SuspendCollection
import matt.collect.suspending.SuspendMutableIterator
import matt.collect.suspending.fake.SuspendFakeMutableIterator
import matt.collect.suspending.fake.SuspendFakeMutableListIterator
import matt.collect.suspending.list.SuspendList
import matt.collect.suspending.list.SuspendMutableList
import matt.collect.suspending.list.SuspendMutableListIterator
import matt.collect.suspending.list.toNonSuspendList
import matt.collect.fake.toFakeMutableList
import matt.lang.err

fun <E> SuspendList<E>.toSuspendingFakeMutableList() = FakeMutableSuspendList(this)

class FakeMutableSuspendList<E>(val list: SuspendList<E>): SuspendMutableList<E> {
  override suspend  fun add(element: E): Boolean {
	err("tried to add in ${FakeMutableSuspendList::class.simpleName}")
  }

  override suspend  fun addAll(elements: SuspendCollection<E>): Boolean {
	err("tried to addAll in ${FakeMutableSuspendList::class.simpleName}")
  }

  override suspend  fun clear() {
	err("tried to clear in ${FakeMutableSuspendList::class.simpleName}")
  }

  override suspend fun addAll(index: Int, elements: SuspendCollection<E>): Boolean {
	err("tried to modify ${FakeMutableSuspendList::class.simpleName}")
  }

  override suspend fun add(index: Int, element: E) {
	err("tried to modify ${FakeMutableSuspendList::class.simpleName}")
  }

  override suspend  fun iterator(): SuspendMutableIterator<E> {
	return SuspendFakeMutableIterator(list.iterator())
  }

  override suspend fun listIterator(): SuspendMutableListIterator<E> {
	return listIterator(0)
  }

  override suspend fun listIterator(index: Int): SuspendMutableListIterator<E> {
	return SuspendFakeMutableListIterator(list.listIterator(index))
  }

  override suspend fun removeAt(index: Int): E {
	err("tried to modify ${FakeMutableSuspendList::class.simpleName}")
  }

  override suspend fun subList(fromIndexInclusive: Int, toIndexExclusive: Int): SuspendMutableList<E> {
	return list.subList(fromIndexInclusive, toIndexExclusive).toSuspendingFakeMutableList()
  }

  override suspend fun get(index: Int): E {
	return list.get(index)
  }

  override suspend fun lastIndexOf(element: E): Int {
	return list.lastIndexOf(element)
  }

  override suspend fun indexOf(element: E): Int {
	return list.indexOf(element)
  }

  override suspend fun size(): Int {
	return list.size()
  }

  override suspend fun isEmpty(): Boolean {
	return list.isEmpty()
  }

  override suspend  fun toNonSuspendCollection(): MutableList<E> {
	return list.toNonSuspendList().toFakeMutableList()
  }

  override suspend fun containsAll(elements: SuspendCollection<E>): Boolean {
	return list.containsAll(elements)
  }

  override suspend fun contains(element: E): Boolean {
	return list.contains(element)
  }

  override suspend fun set(index: Int, element: E): E {
	err("tried to modify ${FakeMutableSuspendList::class.simpleName}")
  }

  override suspend  fun remove(element: E): Boolean {
	err("tried to remove in ${FakeMutableSuspendList::class.simpleName}")
  }

  override suspend  fun removeAll(elements: SuspendCollection<E>): Boolean {
	err("tried to removeAll in ${FakeMutableSuspendList::class.simpleName}")
  }

  override suspend  fun retainAll(elements: SuspendCollection<E>): Boolean {
	err("tried to retainAll in ${FakeMutableSuspendList::class.simpleName}")
  }


}