package matt.collect.suspending.fake

import matt.collect.suspending.SuspendIterator
import matt.collect.suspending.SuspendMutableIterator
import matt.collect.suspending.list.SuspendListIterator
import matt.collect.suspending.list.SuspendMutableListIterator
import matt.lang.common.ILLEGAL
import matt.lang.common.err

fun <E> SuspendIterator<E>.toSuspendableFakeMutableIterator() = SuspendFakeMutableIterator(this)


open class SuspendFakeMutableIterator<E>(val itr: SuspendIterator<E>) :
    SuspendIterator<E> by itr,
    SuspendMutableIterator<E> {
    final override suspend fun remove() {
        err("tried remove in ${SuspendFakeMutableIterator::class.simpleName}")
    }
}

class SuspendFakeMutableListIterator<E>(itr: SuspendListIterator<E>) :
    SuspendListIterator<E> by itr,
    SuspendMutableListIterator<E> {
    override suspend fun add(element: E) = ILLEGAL

    override suspend fun previous() = ILLEGAL
    override suspend fun remove() = ILLEGAL

    override suspend fun set(element: E) = ILLEGAL
}
