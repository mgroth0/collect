package matt.collect.singlen

import matt.collect.single.SingleElementIterator

abstract class SingleElementOrEmptyCollection<E : Any> : Collection<E> {
    abstract val e: E?
    final override val size get() = if (e == null) 0 else 1

    final override fun contains(element: E) = element == e

    final override fun containsAll(elements: Collection<E>) =
        e.let { p ->
            elements.all { it == p }
        }

    final override fun isEmpty() = e == null

    final override fun iterator() =
        e?.let {
            SingleElementIterator(it)
        } ?: emptySet<E>().iterator()
}
