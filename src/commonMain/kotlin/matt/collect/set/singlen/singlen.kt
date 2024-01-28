package matt.collect.set.singlen

import matt.collect.singlen.SingleElementOrEmptyCollection


abstract class SingleElementOrEmptySet<E : Any> : SingleElementOrEmptyCollection<E>(), Set<E> {
//    final override val size get() = super.size
//    final override fun contains(element: E) = super.contains(element)
//    final override fun containsAll(elements: Collection<E>) = super.containsAll(elements)
//    final override fun isEmpty() = super.isEmpty()
//    final override fun iterator() = super.iterator()

    final override fun equals(other: Any?): Boolean {
        if (other !is Set<*>) return false
        if (this.isEmpty()) return other.isEmpty()
        return other.singleOrNull() == e
    }

    final override fun hashCode(): Int {
        return e.hashCode()
    }
}


class ChangingSingleElementOrEmptySet<E : Any>(private val provider: () -> E?) : SingleElementOrEmptySet<E>() {
    override val e get() = provider()
}

class SingleElementSetOrEmptyImpl<E : Any>(override val e: E?) : SingleElementOrEmptySet<E>()

