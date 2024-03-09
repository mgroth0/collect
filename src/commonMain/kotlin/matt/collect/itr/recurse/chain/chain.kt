package matt.collect.itr.recurse.chain

import matt.lang.common.err
import kotlin.contracts.ExperimentalContracts


@ExperimentalContracts
fun <T : Any> T.chain(op: (T) -> T?): Sequence<T> = ChainIterator(this, op).asSequence()


interface RecurseLikeIterator<E> : Iterator<E>

class ChainIterator<T : Any>(
    start: T,
    private val producer: (T) -> T?
) : RecurseLikeIterator<T> {
    private var nextO: T? = start
    private var needsCheck = false
    override fun hasNext(): Boolean {
        if (needsCheck) {
            nextO = producer(nextO!!)
            needsCheck = false
        }
        return nextO != null
    }

    override fun next(): T {
        if (needsCheck) {
            nextO = producer(nextO!!)
            needsCheck = false
        }
        if (nextO != null) {
            needsCheck = true
            return nextO!!
        } else err("bad logic")
    }
}
