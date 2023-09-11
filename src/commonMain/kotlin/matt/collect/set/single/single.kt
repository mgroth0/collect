package matt.collect.set.single

import matt.collect.single.SingleElementCollection
import kotlin.jvm.JvmInline


interface SingleElementSet<E> : SingleElementCollection<E>, Set<E> {
    override val size get() = super.size
    override fun contains(element: E) = super.contains(element)
    override fun containsAll(elements: Collection<E>) = super.containsAll(elements)
    override fun isEmpty() = super.isEmpty()
    override fun iterator() = super.iterator()
}


@JvmInline
value class ChangingSingleElementSet<E>(private val provider: () -> E) : SingleElementSet<E> {
    override val e get() = provider()
}

@JvmInline
value class SingleElementSetImpl<E>(override val e: E) : SingleElementSet<E>

