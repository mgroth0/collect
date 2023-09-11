package matt.collect.list.singlen

import matt.collect.single.SingleElementCollection
import matt.collect.single.SingleElementIterator
import matt.collect.singlen.SingleElementOrEmptyCollection
import matt.lang.NEVER
import kotlin.jvm.JvmInline


interface SingleElementOrEmptyList<E : Any> : SingleElementOrEmptyCollection<E>, List<E> {
    override val size get() = super.size
    override fun contains(element: E) = super.contains(element)
    override fun containsAll(elements: Collection<E>) = super.containsAll(elements)
    override fun isEmpty() = super.isEmpty()
    override fun iterator() = super.iterator()
    override fun listIterator() = listIterator(0)
    override fun listIterator(index: Int): ListIterator<E> {
        require(index in 0..1)
        return e?.let {
            SingleElementIterator(it, got = index == 1)
        } ?: emptyList<E>().listIterator(index)

    }

    override fun indexOf(element: E) = if (e == element) 0 else -1
    override fun lastIndexOf(element: E) = indexOf(element)
    override fun get(index: Int): E {
        if (index != 0) throw IndexOutOfBoundsException()
        return e ?: throw IndexOutOfBoundsException()
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<E> {
        require(fromIndex == 0)
        require(toIndex in 0..1)
        return when (toIndex) {
            0    -> emptyList()
            1    -> {
                check(!isEmpty())
                this
            }

            else -> NEVER
        }
    }
}

@JvmInline
value class ChangingSingleElementOrEmptyList<E : Any>(private val provider: () -> E?) : SingleElementOrEmptyList<E> {
    override val e get() = provider()
}


@JvmInline
value class SingleElementListOrEmptyImpl<E : Any>(override val e: E?) : SingleElementOrEmptyList<E>
