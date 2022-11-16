package matt.collect.list.awaitlist

import java.util.concurrent.ArrayBlockingQueue

class BlockListBuilder<E: Any>(size: Int) {
  private val queue = ArrayBlockingQueue<E>(size)
  val blockList = BlockList(size, queue)
  operator fun plusAssign(e: E) {
	queue.put(e)
  }
}

class BlockList<E: Any>(
  override val size: Int,
  private val queue: ArrayBlockingQueue<E>
): List<E> {

  override fun isEmpty() = size == 0

  private val mem = MutableList<E?>(size) { null }
  private var nextTakeIndex = 0

  @Synchronized
  override fun get(index: Int): E {
	while (nextTakeIndex < index) {
	  val e = queue.take()
	  mem[nextTakeIndex] = e
	  nextTakeIndex++
	}
	if (nextTakeIndex == index) {
	  val e = queue.take()
	  mem[nextTakeIndex] = e
	  nextTakeIndex++
	  return e
	}
	return mem[index]!!
  }

  override fun contains(element: E): Boolean {
	TODO("Not yet implemented")
  }

  override fun containsAll(elements: Collection<E>): Boolean {
	TODO("Not yet implemented")
  }


  override fun iterator(): Iterator<E> = listIterator(0)
  override fun listIterator() = listIterator(0)
  override fun listIterator(index: Int) = object: ListIterator<E> {
	private var nextIdx = index
	override fun hasNext() = nextIdx < size
	override fun hasPrevious(): Boolean {
	  TODO("Not yet implemented")
	}
	override fun next(): E {
	  return get(nextIdx++)
	}
	override fun nextIndex() = nextIdx
	override fun previous(): E {
	  TODO("Not yet implemented")
	}
	override fun previousIndex(): Int {
	  TODO("Not yet implemented")
	}
  }

  override fun subList(fromIndex: Int, toIndex: Int): List<E> {
	TODO("Not yet implemented")
  }

  override fun lastIndexOf(element: E): Int {
	TODO("Not yet implemented")
  }

  override fun indexOf(element: E): Int {
	TODO("Not yet implemented")
  }

}