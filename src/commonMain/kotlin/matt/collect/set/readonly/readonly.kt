package matt.collect.set.readonly

fun <E> Set<E>.readOnly() = ReadOnlySet(this)


class ReadOnlySet<E>(private val inner: Set<E>): Set<E> {
    override val size: Int
        get() = inner.size

    override fun isEmpty(): Boolean = inner.isEmpty()

    override fun iterator(): Iterator<E> = inner.iterator()

    override fun containsAll(elements: Collection<E>): Boolean = inner.containsAll(elements)

    override fun contains(element: E): Boolean = inner.contains(element)

    override fun equals(other: Any?): Boolean = inner == other

    override fun hashCode(): Int = inner.hashCode()
}
