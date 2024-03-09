package matt.collect.itr.recurse.depth

import matt.collect.itr.recurse.AbstractRecursionIterator
import matt.lang.common.NEVER
import matt.lang.common.go


fun <T> T.recursionDepth(rChildren: (T) -> Iterable<T>): Int =
    (
        rChildren(this).maxOfOrNull {
            it.recursionDepth(rChildren)
        } ?: 0
    ) + 1

fun <T> T.recurseWithDepth(
    includeSelf: Boolean = true,
    depth: Int = 0,
    childrenProvider: (T) -> Iterable<T>?
) = DepthRecordingRecursionIterator(
    includeSelf = includeSelf,
    depth = depth,
    childrenProvider = childrenProvider,
    start = this
).asSequence()


class DepthRecordingRecursionIterator<E>(
    start: E,
    includeSelf: Boolean,
    childrenProvider: (E) -> Iterable<E>?,
    private val depth: Int
) : AbstractRecursionIterator<E, Pair<E, Int>>(
        start = start,
        giveSelf = includeSelf,
        childrenProvider = childrenProvider
    ) {


    private val myChildren = childrenProvider(start)?.iterator()
    private var grandChildIterator: Iterator<Pair<E, Int>>? = null


    override fun hasNext() =
        giveSelf
            || (grandChildIterator?.hasNext() == true)
            || (myChildren?.hasNext() == true)

    override fun next(): Pair<E, Int> {


        grandChildIterator?.takeIf { it.hasNext() }?.go {
            return it.next()
        }





        myChildren?.takeIf { it.hasNext() }?.go {
            grandChildIterator =
                myChildren.next().recurseWithDepth(depth = depth + 1, childrenProvider = childrenProvider).iterator()
            return next()
        }







        if (giveSelf) {
            giveSelf = false
            return start to depth
        }
        NEVER
    }
}
