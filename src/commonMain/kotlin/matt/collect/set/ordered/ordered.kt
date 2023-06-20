package matt.collect.set.ordered

import matt.lang.anno.OnlySynchronizedOnJvm
import matt.lang.sync.inSync

fun <E> Iterable<E>.toOrderedSet() = OrderedSet(this)

fun <E> orderedSetOf(vararg e: E): OrderedSet<E> = OrderedSet(setOf(*e))

open class OrderedSet<E>(elements: Iterable<E>) : Set<E> {

    internal val data = elements.toMutableList()

    /*val quickLastIndex = data.lastIndex*/

    override val size: Int @OnlySynchronizedOnJvm get() = data.size

    @OnlySynchronizedOnJvm
    override fun contains(element: E): Boolean {
        return element in data
    }

    @OnlySynchronizedOnJvm
    override fun containsAll(elements: Collection<E>): Boolean {
        return data.containsAll(elements)
    }

    @OnlySynchronizedOnJvm
    override fun isEmpty(): Boolean {
        return data.isEmpty()
    }

    protected var lastChangeStamp = ChangeStamp()
    @OnlySynchronizedOnJvm
    override fun iterator(): Iterator<E> = OrderedSetIterator()

    @OnlySynchronizedOnJvm
    protected fun changeStamp(): ChangeStamp {
        lastChangeStamp = ChangeStamp()
        return lastChangeStamp
    }

    protected open inner class OrderedSetIterator : Iterator<E> {

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


class MutableOrderedSet<E>() : OrderedSet<E>(setOf()), MutableSet<E> {


    @OnlySynchronizedOnJvm
    override fun add(element: E): Boolean {
        if (element in data) {
            return false
        }
        data.add(element)
        changeStamp()
        return true
    }

    @OnlySynchronizedOnJvm
    override fun addAll(elements: Collection<E>): Boolean {
        val eSet = elements.toSet()
        return eSet.map {
            add(it)
        }.any { it }
    }

    @OnlySynchronizedOnJvm
    override fun clear() {
        if (data.isEmpty()) {
            data.clear()
            changeStamp()
        }
    }

    @OnlySynchronizedOnJvm
    override fun iterator(): MutableIterator<E> = MutableOrderedSetIterator()


    private inner class MutableOrderedSetIterator : OrderedSetIterator(), MutableIterator<E> {

        override fun remove() {
            return inSync(this@MutableOrderedSet) {
                if (myChangeStamp != lastChangeStamp) throw ConcurrentModificationException()
                itr.remove()
                myChangeStamp = changeStamp()
            }
        }

    }

    @OnlySynchronizedOnJvm
    override fun retainAll(elements: Collection<E>): Boolean {
        val eSet = elements.toSet()

        val notIn = filter { it !in eSet }

        return removeAll(notIn)

    }

    @OnlySynchronizedOnJvm
    override fun removeAll(elements: Collection<E>): Boolean {
        val eSet = elements.toSet()

        return eSet.map {
            remove(it)
        }.any { it }

    }

    @OnlySynchronizedOnJvm
    override fun remove(element: E): Boolean {

        if (data.remove(element)) {
            changeStamp()
            return true
        }
        return false


    }

}