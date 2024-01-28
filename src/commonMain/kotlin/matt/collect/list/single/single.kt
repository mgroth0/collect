package matt.collect.list.single

import matt.collect.single.SingleElementCollection
import matt.collect.single.SingleElementIterator
import matt.lang.NEVER


abstract class SingleElementList<E> : SingleElementCollection<E>(), List<E> {
    //    final override val size get() = super.size
//    final override fun contains(element: E) = super.contains(element)
//    final override fun containsAll(elements: Collection<E>) = super.containsAll(elements)
//    final override fun isEmpty() = super.isEmpty()
//    final override fun iterator() = super.iterator()
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
            0    -> emptyList()
            1    -> this
            else -> NEVER
        }
    }

    final override fun equals(other: Any?): Boolean {
        if (other !is List<*>) return false

        if (this.isEmpty()) return other.isEmpty()
        if (other.size > 1) return false
        return other.single() == e
    }

    final override fun hashCode(): Int {
        return e.hashCode()
    }
}

class ChangingSingleElementList<E>(private val provider: () -> E) : SingleElementList<E>() {
    override val e get() = provider()
}


class SingleElementListImpl<E>(override val e: E) : SingleElementList<E>()

