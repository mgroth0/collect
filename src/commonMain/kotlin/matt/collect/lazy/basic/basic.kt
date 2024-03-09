package matt.collect.lazy.basic

import matt.lang.anno.JetBrainsYouTrackProject.KT
import matt.lang.anno.YouTrackIssue

@YouTrackIssue(KT, 65555)
object ToUnCommentInK2Beta
/*

 // I ACTUALLY WANT TO KEEP THIS CLASS. IT IS JUST BROKEN IN K2-BETA4. See https://youtrack.jetbrains.com/issue/KT-65555/K2-must-override-spliterator-because-it-inherits-multiple-implementations-for-it. Should be fixed in Beta5.



class MutableLazyList<E>(
    lazyValues: List<LazyValue<E>>
) : BasicLazyList<E>(lazyValues), MutableList<E> {
    override val lazyValues = lazyValues.toMutableList()

    override fun iterator() = FakeMutableIterator(super.iterator())

    override fun listIterator() = FakeMutableListIterator(super.listIterator())

    override fun listIterator(index: Int) = FakeMutableListIterator(super.listIterator(index))

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ) = NOT_IMPLEMENTED

    override fun add(element: E): Boolean {
        lazyValues.add(LazyValue { element })
        return true
    }

    override fun add(
        index: Int,
        element: E
    ) {
        lazyValues.add(index, LazyValue { element })
    }

    override fun addAll(
        index: Int,
        elements: Collection<E>
    ): Boolean {
        lazyValues.addAll(index, elements.map { LazyValue { it } })
        return true
    }

    override fun addAll(elements: Collection<E>): Boolean {
        lazyValues.addAll(elements.map { LazyValue { it } })
        return true
    }

    override fun clear() {
        lazyValues.clear()
    }

    override fun removeAt(index: Int) = NOT_IMPLEMENTED

    override fun set(
        index: Int,
        element: E
    ) = NOT_IMPLEMENTED

    override fun retainAll(elements: Collection<E>): Boolean = lazyValues.removeAll { it.value !in elements }

    override fun removeAll(elements: Collection<E>): Boolean = lazyValues.removeAll { it.value in elements }

    override fun remove(element: E): Boolean {
        lazyValues.iterateM {
            if (it.value == element) {
                remove()
                return true
            }
        }
        return false
    }


}

open class BasicLazyList<E>(
    lazyValues: List<LazyValue<E>>,
    startIndex: Int = 0,
    endIndex: Int? = null
) : LazyList<E> {
    protected open val lazyValues = lazyValues.subList(startIndex, endIndex ?: (lazyValues.size))

    final override val size get() = lazyValues.size

    final override fun get(index: Int) = lazyValues[index].value

    final override fun isEmpty() = lazyValues.isEmpty()

    @Open
    override fun iterator(): Iterator<E> = listIterator()

    @Open
    override fun listIterator() = listIterator(0)

    @Open
    override fun listIterator(index: Int) =
        object : ListIterator<E> {
            private var nextIndex = index

            override fun hasNext() = nextIndex < size

            override fun hasPrevious() = nextIndex > 0

            override fun next(): E {
                if (!hasNext()) throw NoSuchElementException()
                return get(nextIndex++)
            }

            override fun nextIndex(): Int = nextIndex

            override fun previous(): E {
                if (!hasPrevious()) throw NoSuchElementException()
                return get(--nextIndex)
            }

            override fun previousIndex() = nextIndex - 1
        }

    @Open
    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ) = BasicLazyList(lazyValues, fromIndex, toIndex)

    final override fun lastIndexOf(element: E): Int {
        (size - 1 downTo 0).forEach {
            if (get(it) == element) return it
        }
        return -1
    }

    final override fun indexOf(element: E): Int {
        (0..<size).forEach {
            if (get(it) == element) return it
        }
        return -1
    }

    final override fun containsAll(elements: Collection<E>) = elements.all { contains(it) }

    final override fun contains(element: E) = indexOf(element) >= 0
}
*/
