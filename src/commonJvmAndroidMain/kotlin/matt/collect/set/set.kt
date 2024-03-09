
package matt.collect.set

import matt.lang.anno.WorkaroundFor
import java.util.Collections


fun <E> Set<E>.synchronized() = SynchronizedSet(this)
class SynchronizedSet<E>(set: Set<E>) : DelegatedSet<E>() {
    override val delegate: Set<E> = Collections.synchronizedSet(set)
}

fun <E> MutableSet<E>.synchronized() = SynchronizedMutableSet(this)
class SynchronizedMutableSet<E>(set: MutableSet<E>) : DelegatedMutableSet<E>() {
    override val delegate: MutableSet<E> = Collections.synchronizedSet(set)
}


abstract class DelegatedSet<E> : Set<E> {

    protected abstract val delegate: Set<E>
    final override val size get() = delegate.size
    final override fun isEmpty() = delegate.isEmpty()
    final override fun iterator() = delegate.iterator()
    final override fun containsAll(elements: Collection<E>) = delegate.containsAll(elements)
    final override fun contains(element: E) = delegate.contains(element)
}

@WorkaroundFor("https://youtrack.jetbrains.com/issue/KT-18427/Provide-a-way-to-reference-a-delegate-in-class-body")
/*Cannot extend from DelegatedSet, because this returns a different type of iterator and I want to keep that final*/
abstract class DelegatedMutableSet<E> : MutableSet<E> {

    protected abstract val delegate: MutableSet<E>
    final override val size get() = delegate.size
    final override fun isEmpty() = delegate.isEmpty()
    final override fun containsAll(elements: Collection<E>) = delegate.containsAll(elements)
    final override fun contains(element: E) = delegate.contains(element)
    final override fun add(element: E) = delegate.add(element)
    final override fun addAll(elements: Collection<E>) = delegate.addAll(elements)
    final override fun clear() = delegate.clear()
    final override fun iterator() = delegate.iterator()
    final override fun retainAll(elements: Collection<E>) = delegate.retainAll(elements)
    final override fun removeAll(elements: Collection<E>) = delegate.removeAll(elements)
    final override fun remove(element: E) = delegate.remove(element)
}
