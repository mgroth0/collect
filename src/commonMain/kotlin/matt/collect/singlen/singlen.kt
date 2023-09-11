package matt.collect.singlen

import matt.collect.single.SingleElementIterator


interface SingleElementOrEmptyCollection<E : Any> : Collection<E> {
    val e: E?
    override val size get() = if (e == null) 0 else 1
    override fun contains(element: E) = element == e
    override fun containsAll(elements: Collection<E>) = e.let { p ->
        elements.all { it == p }
    }

    override fun isEmpty() = e == null
    override fun iterator() = e?.let {
        SingleElementIterator(it)
    } ?: emptySet<E>().iterator()

}