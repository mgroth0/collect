package matt.collect.list.singlen

import matt.collect.single.SingleElementIterator
import matt.collect.singlen.SingleElementOrEmptyCollection
import matt.lang.common.NEVER

abstract class SingleElementOrEmptyList<E : Any> : SingleElementOrEmptyCollection<E>(), List<E> {
    final override fun listIterator() = listIterator(0)

    final override fun listIterator(index: Int): ListIterator<E> {
        require(index in 0..1)
        return e?.let {
            SingleElementIterator(it, got = index == 1)
        } ?: emptyList<E>().listIterator(index)
    }

    final override fun indexOf(element: E) = if (e == element) 0 else -1

    final override fun lastIndexOf(element: E) = indexOf(element)

    final override fun get(index: Int): E {
        if (index != 0) throw IndexOutOfBoundsException()
        return e ?: throw IndexOutOfBoundsException()
    }

    final override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<E> {
        require(fromIndex == 0)
        require(toIndex in 0..1)
        return when (toIndex) {
            0 -> emptyList()
            1 -> {
                check(!isEmpty())
                this
            }

            else -> NEVER
        }
    }

    final override fun equals(other: Any?): Boolean {
        if (other !is List<*>) return false
        if (isEmpty()) return other.isEmpty()
        return other.singleOrNull() == e
    }

    final override fun hashCode(): Int = e.hashCode()
}

class ChangingSingleElementOrEmptyList<E : Any>(private val provider: () -> E?) : SingleElementOrEmptyList<E>() {
    override val e get() = provider()
}

class SingleElementListOrEmptyImpl<E : Any>(override val e: E?) : SingleElementOrEmptyList<E>()
