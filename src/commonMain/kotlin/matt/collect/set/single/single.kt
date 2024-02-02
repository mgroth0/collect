package matt.collect.set.single

import matt.collect.single.SingleElementCollection

abstract class SingleElementSet<E> : SingleElementCollection<E>(), Set<E> {
//    final override val size get() = super.size
//    final override fun contains(element: E) = super.contains(element)
//    final override fun containsAll(elements: Collection<E>) = super.containsAll(elements)
//    final override fun isEmpty() = super.isEmpty()
//    final override fun iterator() = super.iterator()

    final override fun equals(other: Any?): Boolean {
        if (other !is Set<*>) return false

        if (this.isEmpty()) return other.isEmpty()
        if (other.size > 1) return false
        return other.single() == e
    }

    final override fun hashCode(): Int = e.hashCode()
}

class ChangingSingleElementSet<E>(private val provider: () -> E) : SingleElementSet<E>() {
    override val e get() = provider()
}

class SingleElementSetImpl<E>(override val e: E) : SingleElementSet<E>()
