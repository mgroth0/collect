package matt.collect.suspending.list

import matt.collect.suspending.SuspendCollection
import matt.collect.suspending.SuspendIterator
import matt.collect.suspending.SuspendMutableCollection
import matt.collect.suspending.SuspendMutableIterator
import matt.collect.suspending.SuspendWrapCollection
import matt.collect.suspending.SuspendWrapIterator
import matt.collect.suspending.SuspendWrapMutableCollection
import matt.lang.anno.Open
import matt.lang.setall.setAll

suspend fun <E> SuspendCollection<E>.toNonSuspendList() = when (this) {
    is SuspendList -> toNonSuspendCollection()
    else           -> toNonSuspendCollection().toList()
}


interface SuspendList<out E> : SuspendCollection<E> {

    suspend fun get(index: Int): E

    suspend fun indexOf(element: @UnsafeVariance E): Int

    suspend fun lastIndexOf(element: @UnsafeVariance E): Int


    suspend fun listIterator(): SuspendListIterator<E>

    suspend fun listIterator(index: Int): SuspendListIterator<E>

    suspend fun subList(
        fromIndexInclusive: Int,
        toIndexExclusive: Int
    ): SuspendList<E>

    override suspend fun toNonSuspendCollection(): List<E>

}


fun <E> List<E>.suspending() = SuspendWrapList(this)

open class SuspendWrapList<E>(private val list: List<E>) : SuspendWrapCollection<E>(list), SuspendList<E> {


    final override suspend fun get(index: Int): E = list[index]

    final override suspend fun indexOf(element: E): Int = list.indexOf(element)

    final override suspend fun lastIndexOf(element: E): Int = list.lastIndexOf(element)

    @Open
    override suspend fun listIterator(): SuspendListIterator<E> = SuspendWrapListIterator(list.listIterator())

    @Open
    override suspend fun listIterator(index: Int): SuspendListIterator<E> = SuspendWrapListIterator(list.listIterator(index))

    @Open
    override suspend fun subList(
        fromIndexInclusive: Int,
        toIndexExclusive: Int
    ): SuspendList<E> = SuspendWrapList(list.subList(fromIndexInclusive, toIndexExclusive))

    @Open
    override suspend fun toNonSuspendCollection(): List<E> = list.toList()
}


interface SuspendListIterator<out E> : SuspendIterator<E> {
    suspend fun hasPrevious(): Boolean
    suspend fun nextIndex(): Int
    suspend fun previous(): E
    suspend fun previousIndex(): Int
}

fun <E> ListIterator<E>.suspending() = SuspendWrapListIterator(this)

open class SuspendWrapListIterator<E>(private val itr: ListIterator<E>) : SuspendWrapIterator<E>(itr),
    SuspendListIterator<E> {
    final override suspend fun hasPrevious(): Boolean = itr.hasPrevious()

    final override suspend fun nextIndex(): Int = itr.nextIndex()

    final override suspend fun previous(): E = itr.previous()

    final override suspend fun previousIndex(): Int = itr.previousIndex()

}


interface SuspendMutableList<E> : SuspendList<E>, SuspendMutableCollection<E> {

    suspend fun addAll(
        index: Int,
        elements: SuspendCollection<E>
    ): Boolean

    suspend fun add(
        index: Int,
        element: E
    )


    override suspend fun iterator(): SuspendMutableIterator<E>

    override suspend fun listIterator(): SuspendMutableListIterator<E>

    override suspend fun listIterator(index: Int): SuspendMutableListIterator<E>

    suspend fun removeAt(index: Int): E

    override suspend fun subList(
        fromIndexInclusive: Int,
        toIndexExclusive: Int
    ): SuspendMutableList<E>

    suspend fun set(
        index: Int,
        element: E
    ): E

    override suspend fun toNonSuspendCollection(): MutableList<E>

}

fun <E> MutableList<E>.suspending() = SuspendWrapMutableList(this)


class SuspendWrapMutableList<E>(private val list: MutableList<E>) : SuspendWrapList<E>(list), SuspendMutableList<E> {
    private val mutColSuper = SuspendWrapMutableCollection(list)
    override suspend fun add(element: E): Boolean = mutColSuper.add(element)

    override suspend fun addAll(elements: SuspendCollection<E>): Boolean = mutColSuper.addAll(elements)

    override suspend fun addAll(
        index: Int,
        elements: SuspendCollection<E>
    ): Boolean = list.addAll(index, elements.toNonSuspendList())

    override suspend fun add(
        index: Int,
        element: E
    ) = list.add(index, element)

    override suspend fun setAll(c: Collection<E>) = list.setAll(c)

    override suspend fun iterator(): SuspendMutableIterator<E> = SuspendWrapMutableListIterator(list.listIterator())

    override suspend fun listIterator(): SuspendMutableListIterator<E> = SuspendWrapMutableListIterator(list.listIterator())

    override suspend fun listIterator(index: Int): SuspendMutableListIterator<E> = SuspendWrapMutableListIterator(list.listIterator(index))

    override suspend fun removeAt(index: Int): E = list.removeAt(index)

    override suspend fun subList(
        fromIndexInclusive: Int,
        toIndexExclusive: Int
    ): SuspendMutableList<E> = SuspendWrapMutableList(list.subList(fromIndexInclusive, toIndexExclusive))

    override suspend fun set(
        index: Int,
        element: E
    ): E = list.set(index, element)

    override suspend fun clear() = list.clear()

    override suspend fun retainAll(elements: SuspendCollection<E>): Boolean = list.retainAll(elements.toNonSuspendList())

    override suspend fun removeAll(elements: SuspendCollection<E>): Boolean = list.removeAll(elements.toNonSuspendList())

    override suspend fun remove(element: E): Boolean = list.remove(element)

    override suspend fun toNonSuspendCollection(): MutableList<E> = list.toMutableList()
}


interface SuspendMutableListIterator<E> : SuspendListIterator<E>, SuspendMutableIterator<E> {
    suspend fun add(element: E)
    suspend fun set(element: E)
}

fun <E> MutableListIterator<E>.suspending() = SuspendWrapMutableListIterator(this)

class SuspendWrapMutableListIterator<E>(private val itr: MutableListIterator<E>) : SuspendWrapListIterator<E>(itr),
    SuspendMutableListIterator<E> {
    override suspend fun add(element: E) = itr.add(element)

    override suspend fun set(element: E) = itr.set(element)

    override suspend fun remove() = itr.remove()


}
