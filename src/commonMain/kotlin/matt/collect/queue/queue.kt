package matt.collect.queue

import matt.collect.list.linked.MyLinkedList
import matt.lang.sync.common.ReferenceMonitor
import matt.lang.sync.common.inSync

interface MyQueue<E : Any> : Collection<E> {
    fun poll(): E?

    fun offer(e: E): Boolean

    fun peek(): E

    fun element(): E
}

interface MyMutableQueue<E : Any> : MyQueue<E>, MutableCollection<E>

class MyMutableQueueImpl<E : Any>() : MyMutableQueue<E>, ReferenceMonitor {
    private val list = MyLinkedList<E>()

    override fun poll(): E? =
        inSync {
            return list.removeFirstOrNull()
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

    override fun isEmpty(): Boolean {
        TODO()
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

    /*In java, this would return the first element or throw an exception:

    java: (NOT THIS):  Retrieves and removes the head of this queue. This method differs from poll only in that it throws an exception if this queue is empty.


    to get java remove behavior, use `removeJavaStyle`


     * */
    override fun remove(element: E): Boolean {
        TODO()
    }

    override fun addAll(elements: Collection<E>): Boolean {
        TODO()
    }

    override fun add(element: E): Boolean =
        inSync {
            list.add(element)
            return true
        }

    override fun containsAll(elements: Collection<E>): Boolean {
        TODO()
    }

    override fun contains(element: E): Boolean {
        TODO()
    }
}

fun <E : Any> MyQueue<E>.removeJavaStyle() = poll() ?: throw NoSuchElementException("queue is empty")

fun <E : Any> MyQueue<E>.pollUntilEnd(): List<E> {
    val list = mutableListOf<E>()
    do {
        val e = poll()
        if (e != null) {
            list += e
        }
    } while (e != null)
    return list
}

fun <E : Any> MyQueue<E>.pollSequence() =
    sequence<E> {
        do {
            val e = poll()
            if (e != null) yield(e)
        } while (e != null)
    }
