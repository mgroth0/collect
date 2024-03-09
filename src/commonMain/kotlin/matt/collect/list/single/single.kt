package matt.collect.list.single

import matt.collect.single.SingleElementCollection
import matt.collect.single.SingleElementIterator
import matt.lang.common.NEVER

abstract class SingleElementList<E> : SingleElementCollection<E>(), List<E> {
    final override fun listIterator() = listIterator(0)

    final override fun listIterator(index: Int): ListIterator<E> {
        require(index in 0..1)
        return SingleElementIterator(e, got = index == 1)
    }

    final override fun indexOf(element: E) = if (e == element) 0 else -1

    final override fun lastIndexOf(element: E) = indexOf(element)

    final override fun get(index: Int): E {
        if (index != 0) throw IndexOutOfBoundsException()
        return e
    }

    final override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<E> {
        require(fromIndex == 0)
        require(toIndex in 0..1)
        return when (toIndex) {
            0 -> emptyList()
            1 -> this
            else -> NEVER
        }
    }

    final override fun equals(other: Any?): Boolean {
        if (other !is List<*>) return false

        if (isEmpty()) return other.isEmpty()
        if (other.size > 1) return false
        return other.single() == e
    }

    final override fun hashCode(): Int = e.hashCode()
}

class ChangingSingleElementList<E>(private val provider: () -> E) : SingleElementList<E>() {
    override val e get() = provider()
}

class SingleElementListImpl<E>(override val e: E) : SingleElementList<E>()
