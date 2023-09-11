package matt.collect.set.singlen

import matt.collect.singlen.SingleElementOrEmptyCollection
import kotlin.jvm.JvmInline


interface SingleElementOrEmptySet<E : Any> : SingleElementOrEmptyCollection<E>, Set<E> {
    override val size get() = super.size
    override fun contains(element: E) = super.contains(element)
    override fun containsAll(elements: Collection<E>) = super.containsAll(elements)
    override fun isEmpty() = super.isEmpty()
    override fun iterator() = super.iterator()
}


@JvmInline
value class ChangingSingleElementOrEmptySet<E : Any>(private val provider: () -> E?) : SingleElementOrEmptySet<E> {
    override val e get() = provider()
}

@JvmInline
value class SingleElementSetOrEmptyImpl<E : Any>(override val e: E?) : SingleElementOrEmptySet<E>

