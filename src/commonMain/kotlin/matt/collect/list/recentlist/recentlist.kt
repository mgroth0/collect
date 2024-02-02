package matt.collect.list.recentlist

import matt.collect.itr.FakeMutableIterator
import matt.collect.queue.MyMutableQueue
import matt.collect.queue.MyMutableQueueImpl
import matt.collect.queue.removeJavaStyle
import matt.lang.sync.ReferenceMonitor
import matt.lang.sync.inSync

class EvitctingQueue<E : Any>(val capacity: Int) : MyMutableQueue<E>, ReferenceMonitor {
    private val data = MyMutableQueueImpl<E>()

    override fun add(element: E): Boolean =
        inSync {
            data.add(element)
            if (data.size > capacity) data.removeJavaStyle()
            return true
        }

    override fun addAll(elements: Collection<E>): Boolean {
        TODO()
    }

    override fun clear() {
        TODO()
    }

    override fun iterator() = FakeMutableIterator<E>(data.iterator())

    override fun retainAll(elements: Collection<E>): Boolean {
        TODO()
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        TODO()
    }

    override fun remove(element: E): Boolean {
        TODO()
    }

    override fun isEmpty(): Boolean {
        TODO()
    }

    override fun poll() = inSync { data.poll() }

    override fun element(): E {
        TODO()
    }

    override fun peek() = inSync { data.peek() }

    override fun offer(e: E): Boolean {
        TODO()
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        TODO()
    }

    override fun contains(element: E): Boolean {
        TODO()
    }

    override val size: Int get() = data.size
}
