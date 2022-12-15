package matt.collect.set.ordered

import matt.lang.sync.inSync
import kotlin.jvm.Synchronized

fun <E> Iterable<E>.toOrderedSet() = OrderedSet(this)

open class OrderedSet<E>(elements: Iterable<E>): Set<E> {

  internal val data = elements.toMutableList()


  override val size: Int
	@Synchronized get() = data.size

  @Synchronized override fun contains(element: E): Boolean {
	return element in data
  }

  @Synchronized override fun containsAll(elements: Collection<E>): Boolean {
	return data.containsAll(elements)
  }

  @Synchronized override fun isEmpty(): Boolean {
	return data.isEmpty()
  }

  protected var lastChangeStamp = ChangeStamp()
  @Synchronized override fun iterator(): Iterator<E> = OrderedSetIterator()

  @Synchronized protected fun changeStamp(): ChangeStamp {
	lastChangeStamp = ChangeStamp()
	return lastChangeStamp
  }

  protected open inner class OrderedSetIterator: Iterator<E> {

	protected var myChangeStamp = lastChangeStamp
	protected val itr = data.iterator()

	override fun hasNext(): Boolean {
	  return inSync(this@OrderedSet) {
		if (myChangeStamp != lastChangeStamp) throw ConcurrentModificationException()
		itr.hasNext()
	  }
	}

	override fun next(): E {
	  return inSync(this@OrderedSet) {
		if (myChangeStamp != lastChangeStamp) throw ConcurrentModificationException()
		itr.next()
	  }
	}
  }


  protected class ChangeStamp

}


class MutableOrderedSet<E>(): OrderedSet<E>(setOf()), MutableSet<E> {


  @Synchronized override fun add(element: E): Boolean {
	if (element in data) {
	  return false
	}
	data.add(element)
	changeStamp()
	return true
  }

  @Synchronized override fun addAll(elements: Collection<E>): Boolean {
	val eSet = elements.toSet()
	return eSet.map {
	  add(it)
	}.any { it }
  }

  @Synchronized override fun clear() {
	if (data.isEmpty()) {
	  data.clear()
	  changeStamp()
	}
  }

  @Synchronized override fun iterator(): MutableIterator<E> = MutableOrderedSetIterator()


  private inner class MutableOrderedSetIterator: OrderedSetIterator(), MutableIterator<E> {

	override fun remove() {
	  return inSync(this@MutableOrderedSet) {
		if (myChangeStamp != lastChangeStamp) throw ConcurrentModificationException()
		itr.remove()
		myChangeStamp = changeStamp()
	  }
	}

  }

  @Synchronized
  override fun retainAll(elements: Collection<E>): Boolean {
	val eSet = elements.toSet()

	val notIn = filter { it !in eSet }

	return removeAll(notIn)

  }

  @Synchronized override fun removeAll(elements: Collection<E>): Boolean {
	val eSet = elements.toSet()

	return eSet.map {
	  remove(it)
	}.any { it }

  }

  @Synchronized override fun remove(element: E): Boolean {

	if (data.remove(element)) {
	  changeStamp()
	  return true
	}
	return false


  }

}