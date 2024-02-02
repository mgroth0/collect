package matt.collect.itr.chain

import matt.collect.itr.areAllUnique
import matt.collect.itr.subList

fun <E> List<E>.circularChain(): ChainLink<E> {
    require(size >= 2) {
        "circular chain size should be greater than or equal to 2, but it is $size"
    }
    val firstLink = ChainLink(first(), isStart = true)
    var lastLink: ChainLink<E> = firstLink
    val allLinks = mutableSetOf(firstLink)
    subList(1).forEach {
        val link = ChainLink(it, isStart = false)
        lastLink.mutableNext = link
        link.mutablePrevious = lastLink
        lastLink = link
        allLinks.add(link)
    }
    lastLink.mutableNext = firstLink
    firstLink.mutablePrevious = lastLink

    return firstLink
}

class ChainLink<E>(
    val element: E,
    isStart: Boolean,
) {
    var isStart = isStart
        private set
    internal var mutableNext: ChainLink<E>? = null
    internal var mutablePrevious: ChainLink<E>? = null
    val next get() = mutableNext!!
    val previous get() = mutablePrevious!!

    fun remove() {
        check(previous != next)
        previous.mutableNext = next
        next.mutablePrevious = previous
        if (this.isStart) {
            next.isStart = true
        }
        mutableNext = null
        mutablePrevious = null
    }
}

fun <E> ChainLink<E>.toCursor() = SimpleCursor(this)

fun <E> SimpleCursor<E>.toIndexedCursor() = IndexedCursor(this)

interface Cursor<E> {
    fun next()

    fun previous()

    fun remove()

    fun isStart(): Boolean

    fun element(): E

    fun countChainSize(): Int
}

class SimpleCursor<E>(internal var link: ChainLink<E>) : Cursor<E> {
    override fun next() {
        link = link.next
    }

    override fun previous() {
        link = link.previous
    }

    override fun remove() {
        val oldLink = link
        next()
        oldLink.remove()
    }

    override fun isStart() = link.isStart

    override fun element() = link.element

    override fun countChainSize(): Int {
        var size = 1
        var currentLink = link

        do {
            currentLink = currentLink.next
            size += 1
        } while (currentLink != link)

        return size
    }
}

class IndexedCursor<E>(private val cursor: SimpleCursor<E>) : Cursor<E> by cursor {
    private val index =
        run {
            val r = mutableMapOf<E, MutableList<ChainLink<E>>>()
            val origLink = cursor.link
            do {
                r.getOrPut(cursor.element()) {
                    mutableListOf()
                }.add(cursor.link)
                cursor.next()
            } while (cursor.link != origLink)
            r
        }

    fun removeFirst(element: E) {
        val link = index[element]!!.removeFirst()
        if (cursor.link == link) {
            cursor.next()
        }
        link.remove()
    }

    fun removeFirstOfEach(vararg elements: E) = removeFirstOfEach(elements.toList())

    fun removeFirstOfEach(elements: Collection<E>) {
        require(elements.toList().areAllUnique())
        elements.forEach { removeFirst(it) }
    }
}
