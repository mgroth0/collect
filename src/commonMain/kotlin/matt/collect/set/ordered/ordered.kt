package matt.collect.set.ordered

import matt.lang.anno.Open
import matt.lang.sync.common.ReferenceMonitor
import matt.lang.sync.common.inSync
import matt.lang.sync.inSync

fun <E> Iterable<E>.toOrderedSet() = OrderedSet(this)

fun <E> orderedSetOf(vararg e: E): OrderedSet<E> = OrderedSet(setOf(*e))

open class OrderedSet<E>(elements: Iterable<E>) : Set<E>, ReferenceMonitor {

    internal val data = elements.toMutableList()

    /*val quickLastIndex = data.lastIndex*/

    final override val size: Int get() = inSync { data.size }

    final override fun contains(element: E): Boolean =
        inSync {
            return element in data
        }

    final override fun containsAll(elements: Collection<E>): Boolean =
        inSync {
            return data.containsAll(elements)
        }

    final override fun isEmpty(): Boolean =
        inSync {
            return data.isEmpty()
        }

    protected var lastChangeStamp = ChangeStamp()

    @Open
    override fun iterator(): Iterator<E> = inSync { OrderedSetIterator() }

    protected fun changeStamp(): ChangeStamp =
        inSync {
            lastChangeStamp = ChangeStamp()
            return lastChangeStamp
        }

    protected open inner class OrderedSetIterator : Iterator<E> {

        protected var myChangeStamp = lastChangeStamp
        protected val itr = data.iterator()

        final override fun hasNext(): Boolean =
            inSync(this@OrderedSet) {
                if (myChangeStamp != lastChangeStamp) throw ConcurrentModificationException()
                itr.hasNext()
            }

        final override fun next(): E =
            inSync(this@OrderedSet) {
                if (myChangeStamp != lastChangeStamp) throw ConcurrentModificationException()
                itr.next()
            }
    }


    protected class ChangeStamp
}


class MutableOrderedSet<E>() : OrderedSet<E>(setOf()), MutableSet<E> {


    override fun add(element: E): Boolean =
        inSync {
            if (element in data) {
                return false
            }
            data.add(element)
            changeStamp()
            return true
        }

    override fun addAll(elements: Collection<E>): Boolean =
        inSync {
            val eSet = elements.toSet()
            return eSet.map {
                add(it)
            }.any { it }
        }

    override fun clear() =
        inSync {
            if (data.isEmpty()) {
                data.clear()
                changeStamp()
            }
        }

    override fun iterator(): MutableIterator<E> = inSync { MutableOrderedSetIterator() }


    private inner class MutableOrderedSetIterator : OrderedSetIterator(), MutableIterator<E> {

        override fun remove() =
            inSync(this@MutableOrderedSet) {
                if (myChangeStamp != lastChangeStamp) throw ConcurrentModificationException()
                itr.remove()
                myChangeStamp = changeStamp()
            }
    }

    override fun retainAll(elements: Collection<E>): Boolean =
        inSync {
            val eSet = elements.toSet()

            val notIn = filter { it !in eSet }

            return removeAll(notIn)
        }

    override fun removeAll(elements: Collection<E>): Boolean =
        inSync {
            val eSet = elements.toSet()

            return eSet.map {
                remove(it)
            }.any { it }
        }

    override fun remove(element: E): Boolean =
        inSync {

            if (data.remove(element)) {
                changeStamp()
                return true
            }
            return false
        }
}
