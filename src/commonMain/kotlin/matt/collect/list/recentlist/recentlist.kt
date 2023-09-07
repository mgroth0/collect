package matt.collect.list.recentlist

import matt.collect.itr.FakeMutableIterator
import matt.collect.queue.MyMutableQueue
import matt.collect.queue.MyMutableQueueImpl
import matt.collect.queue.removeJavaStyle
import matt.lang.anno.OnlySynchronizedOnJvm


class EvitctingQueue<E : Any>(val capacity: Int) : MyMutableQueue<E> {


    private val data = MyMutableQueueImpl<E>()

    @OnlySynchronizedOnJvm
    override fun add(element: E): Boolean {
        data.add(element)
        if (data.size > capacity) data.removeJavaStyle()
        return true
    }

    override fun addAll(elements: Collection<E>): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun iterator() = FakeMutableIterator<E>(data.iterator())


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

    @OnlySynchronizedOnJvm
    override fun poll() = data.poll()

    override fun element(): E {
        TODO("Not yet implemented")
    }

    @OnlySynchronizedOnJvm
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


