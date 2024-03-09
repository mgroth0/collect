package matt.collect.list.awaitlist

import matt.collect.list.StructuralList
import java.util.concurrent.ArrayBlockingQueue

class BlockListBuilder<E : Any>(size: Int) {
    private val queue = ArrayBlockingQueue<E>(size)
    val blockList = BlockList(size, queue)

    operator fun plusAssign(e: E) {
        queue.put(e)
    }
}

class BlockList<E : Any>(
    override val size: Int,
    private val queue: ArrayBlockingQueue<E>
) : StructuralList<E>(), List<E> {
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
        TODO()
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        TODO()
    }

    override fun iterator(): Iterator<E> = listIterator(0)

    override fun listIterator() = listIterator(0)

    override fun listIterator(index: Int) =
        object : ListIterator<E> {
            private var nextIdx = index

            override fun hasNext() = nextIdx < size

            override fun hasPrevious(): Boolean {
                TODO()
            }

            override fun next(): E = get(nextIdx++)

            override fun nextIndex() = nextIdx

            override fun previous(): E {
                TODO()
            }

            override fun previousIndex(): Int {
                TODO()
            }
        }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<E> {
        TODO()
    }

    override fun lastIndexOf(element: E): Int {
        TODO()
    }

    override fun indexOf(element: E): Int {
        TODO()
    }
}
