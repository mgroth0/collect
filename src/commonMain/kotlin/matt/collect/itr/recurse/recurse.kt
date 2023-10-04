package matt.collect.itr.recurse

import matt.collect.itr.recurse.chain.RecurseLikeIterator
import matt.lang.NEVER

const val DEFAULT_INCLUDE_SELF = true

fun <T> T.recurseChildren(rChildren: (T) -> Iterable<T>) = recurse(includeSelf = false, rChildren)

fun <T> T.recurseToFlat(
    rChildren: (T) -> Iterable<T>,
    r: MutableList<T>? = null
): List<T> {
    var rr = r
    if (rr == null) {
        rr = mutableListOf()
    }
    rChildren(this).forEach {
        it.recurseToFlat(rChildren, rr)
    }
    rr.add(this)
    return rr
}


fun <T> T.recurse(
    includeSelf: Boolean = DEFAULT_INCLUDE_SELF,
    rChildren: (T) -> Iterable<T>?
): Sequence<T> = RecursionIterator(
    start = this, includeSelf = includeSelf, childrenProvider = rChildren
).asSequence()

fun <T> T.recurseArrays(
    includeSelf: Boolean = DEFAULT_INCLUDE_SELF,
    rChildren: (T) -> Array<T>?
): Sequence<T> = RecursionArrayIterator(
    start = this, includeSelf = includeSelf, childrenProvider = rChildren
).asSequence()


class RecursionIterator<E>(
    start: E,
    private val includeSelf: Boolean,
    childrenProvider: (E) -> Iterable<E>?
) : AbstractRecursionIterator<E, E>(start = start, giveSelf = includeSelf, childrenProvider = childrenProvider) {


    private var doesHaveNext = true
    override fun hasNext() = doesHaveNext


    private var nextNode = childrenProvider(start)?.iterator()?.takeIf { it.hasNext() }?.let {
        Itr(parent = if (includeSelf) Self() else null, it)
    } ?: if (includeSelf) Self() else run {
        doesHaveNext = false
        None
    }

    override fun next() = nextNode.next()


    private interface Node<out E> {
        fun next(): E
    }

    private inner class Itr(
        val parent: Node<E>? = null,
        val itr: Iterator<E>
    ) : Node<E> {

        private var entered: E? = null
        override fun next(): E {
            val n = entered ?: itr.next()
            if (entered == null) {
                childrenProvider(n)?.iterator()?.takeIf { it.hasNext() }?.let { children ->
                    entered = n
                    nextNode = Itr(this, children)
                    return nextNode.next()
                }
            }
            entered = null
            nextNode = when {
                itr.hasNext() -> this
                else          -> parent ?: when {
                    includeSelf -> Self()
                    else        -> {
                        doesHaveNext = false
                        None
                    }
                }
            }
            return n
        }
    }


    private inner class Self : Node<E> {
        override fun next(): E {
            doesHaveNext = false
            return start
        }
    }

    private object None : Node<Nothing> {
        override fun next() = NEVER
    }


}


abstract class AbstractRecursionIterator<E, R>(
    protected val start: E,
    protected var giveSelf: Boolean,
    protected val childrenProvider: (E) -> Iterable<E>?
) : RecurseLikeIterator<R> {/*The need to optimize is too great, so I could not generalize this with the depth-counting implementation just yet.*/
}






class RecursionArrayIterator<E>(
    start: E,
    private val includeSelf: Boolean,
    childrenProvider: (E) -> Array<E>?
) : AbstractRecursionArrayIterator<E, E>(start = start, giveSelf = includeSelf, childrenProvider = childrenProvider) {


    private var doesHaveNext = true
    override fun hasNext() = doesHaveNext


    private var nextNode = childrenProvider(start)?.iterator()?.takeIf { it.hasNext() }?.let {
        Itr(parent = if (includeSelf) Self() else null, it)
    } ?: if (includeSelf) Self() else run {
        doesHaveNext = false
        None
    }

    override fun next() = nextNode.next()


    private interface Node<out E> {
        fun next(): E
    }

    private inner class Itr(
        val parent: Node<E>? = null,
        val itr: Iterator<E>
    ) : Node<E> {

        private var entered: E? = null
        override fun next(): E {
            val n = entered ?: itr.next()
            if (entered == null) {
                childrenProvider(n)?.iterator()?.takeIf { it.hasNext() }?.let { children ->
                    entered = n
                    nextNode = Itr(this, children)
                    return nextNode.next()
                }
            }
            entered = null
            nextNode = when {
                itr.hasNext() -> this
                else          -> parent ?: when {
                    includeSelf -> Self()
                    else        -> {
                        doesHaveNext = false
                        None
                    }
                }
            }
            return n
        }
    }


    private inner class Self : Node<E> {
        override fun next(): E {
            doesHaveNext = false
            return start
        }
    }

    private object None : Node<Nothing> {
        override fun next() = NEVER
    }


}


abstract class AbstractRecursionArrayIterator<E, R>(
    protected val start: E,
    protected var giveSelf: Boolean,
    protected val childrenProvider: (E) -> Array<E>?
) : RecurseLikeIterator<R> {/*The need to optimize is too great, so I could not generalize this with the depth-counting implementation just yet.*/
}
