package matt.collect.queue

import java.util.*

class JQueueWrapper<E : Any>(private val jQueue: Queue<E>) : MyMutableQueue<E> {

    @Synchronized
    override fun poll(): E? {
        return jQueue.poll()
    }

    override fun peek(): E {
        TODO("Not yet implemented")
    }

    override fun element(): E {
        TODO("Not yet implemented")
    }

    override fun offer(e: E): Boolean {
        TODO("Not yet implemented")
    }

    override val size: Int
        get() = TODO("Not yet implemented")

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): MutableIterator<E> {
        TODO("Not yet implemented")
    }

    override fun clear() {
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

    override fun addAll(elements: Collection<E>): Boolean {
        TODO("Not yet implemented")
    }

    @Synchronized
    override fun add(element: E): Boolean {
        return jQueue.add(element)
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(element: E): Boolean {
        TODO("Not yet implemented")
    }
}


