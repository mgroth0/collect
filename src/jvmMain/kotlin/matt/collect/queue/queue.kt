package matt.collect.queue

import java.util.Queue

class JQueueWrapper<E : Any>(private val jQueue: Queue<E>) : MyMutableQueue<E> {

    @Synchronized
    override fun poll(): E? {
        return jQueue.poll()
    }

    override fun peek(): E {
        TODO()
    }

    override fun element(): E {
        TODO()
    }

    override fun offer(e: E): Boolean {
        TODO()
    }

    override val size: Int
        get() = TODO()

    @Synchronized
    override fun isEmpty(): Boolean {
        return jQueue.isEmpty()
    }

    override fun iterator(): MutableIterator<E> {
        TODO()
    }

    override fun clear() {
        TODO()
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        TODO()
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        TODO()
    }

    override fun remove(element: E): Boolean {
        TODO()
    }

    override fun addAll(elements: Collection<E>): Boolean {
        TODO()
    }

    @Synchronized
    override fun add(element: E): Boolean {
        return jQueue.add(element)
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        TODO()
    }

    override fun contains(element: E): Boolean {
        TODO()
    }
}


