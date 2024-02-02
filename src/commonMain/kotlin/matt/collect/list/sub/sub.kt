package matt.collect.list.sub

import matt.collect.list.StructuralList

class SubList<E>() : StructuralList<E>(), List<E> {
    override val size: Int
        get() = TODO()

    override fun contains(element: E): Boolean {
        TODO()
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        TODO()
    }

    override fun get(index: Int): E {
        TODO()
    }

    override fun indexOf(element: E): Int {
        TODO()
    }

    override fun isEmpty(): Boolean {
        TODO()
    }

    override fun iterator(): Iterator<E> {
        TODO()
    }

    override fun lastIndexOf(element: E): Int {
        TODO()
    }

    override fun listIterator(): ListIterator<E> {
        TODO()
    }

    override fun listIterator(index: Int): ListIterator<E> {
        TODO()
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int,
    ): List<E> {
        TODO()
    }
}
