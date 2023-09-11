package matt.collect.single


interface SingleElementCollection<E> : Collection<E> {
    val e: E
    override val size get() = 1
    override fun contains(element: E) = element == e
    override fun containsAll(elements: Collection<E>) = e.let { p ->
        elements.all { it == p }
    }

    override fun isEmpty() = false
    override fun iterator() = SingleElementIterator(e)
}


class SingleElementIterator<E>(
    private val e: E,
    private var got: Boolean = false
) : ListIterator<E> {

    override fun hasNext() = !got

    override fun hasPrevious() = got

    override fun next(): E {
        check(!got)
        got = true
        return e
    }

    override fun nextIndex(): Int {
        check(!got)
        return 1
    }


    override fun previous(): E {
        check(got)
        got = false
        return e
    }

    override fun previousIndex(): Int {
        check(got)
        return 0
    }
}