package matt.collect.list.single

import matt.lang.anno.OnlySynchronizedOnJvm
import matt.lang.require.requireNot
import kotlin.jvm.JvmInline

@JvmInline
value class SingleElementList<E>(private val e: E) : List<E> {
    override val size: Int
        get() = 1

    override fun contains(element: E): Boolean {
        return element == e
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        return elements.all { it == e }
    }

    override fun get(index: Int): E {
        if (index == 0) {
            return e
        } else {
            throw IndexOutOfBoundsException()
        }
    }

    override fun indexOf(element: E): Int {
        if (e == element) return 0 else return -1
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun iterator() = object : Iterator<E> {


        private var didNext = false

        @OnlySynchronizedOnJvm
        override fun hasNext(): Boolean {
            return !didNext
        }

        @OnlySynchronizedOnJvm
        override fun next(): E {
            requireNot(didNext)
            didNext = true
            return e
        }

    }

    override fun lastIndexOf(element: E): Int {
        if (element == e) return 0 else return -1
    }

    override fun listIterator(): ListIterator<E> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): ListIterator<E> {
        TODO("Not yet implemented")
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<E> {
        TODO("Not yet implemented")
    }
}