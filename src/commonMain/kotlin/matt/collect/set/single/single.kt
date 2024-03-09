package matt.collect.set.single

import matt.collect.single.SingleElementCollection

abstract class SingleElementSet<E> : SingleElementCollection<E>(), Set<E> {
    final override fun equals(other: Any?): Boolean {
        if (other !is Set<*>) return false

        if (isEmpty()) return other.isEmpty()
        if (other.size > 1) return false
        return other.single() == e
    }

    final override fun hashCode(): Int = e.hashCode()
}

class ChangingSingleElementSet<E>(private val provider: () -> E) : SingleElementSet<E>() {
    override val e get() = provider()
}

class SingleElementSetImpl<E>(override val e: E) : SingleElementSet<E>()
