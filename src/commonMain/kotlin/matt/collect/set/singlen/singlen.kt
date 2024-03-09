package matt.collect.set.singlen

import matt.collect.singlen.SingleElementOrEmptyCollection

abstract class SingleElementOrEmptySet<E : Any> : SingleElementOrEmptyCollection<E>(), Set<E> {
    final override fun equals(other: Any?): Boolean {
        if (other !is Set<*>) return false
        if (isEmpty()) return other.isEmpty()
        return other.singleOrNull() == e
    }

    final override fun hashCode(): Int = e.hashCode()
}

class ChangingSingleElementOrEmptySet<E : Any>(private val provider: () -> E?) : SingleElementOrEmptySet<E>() {
    override val e get() = provider()
}

class SingleElementSetOrEmptyImpl<E : Any>(override val e: E?) : SingleElementOrEmptySet<E>()
