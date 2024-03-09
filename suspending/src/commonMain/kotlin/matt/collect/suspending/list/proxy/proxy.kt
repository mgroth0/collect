package matt.collect.suspending.list.proxy

import matt.collect.suspending.SuspendCollection
import matt.collect.suspending.ext.map
import matt.collect.suspending.list.SuspendMutableList
import matt.collect.suspending.list.SuspendMutableListIterator
import matt.collect.suspending.list.fake.toSuspendingFakeMutableList
import matt.lang.anno.JetBrainsYouTrackProject.KT
import matt.lang.anno.YouTrackIssue
import matt.lang.convert.BiConverter
import matt.model.data.proxy.list.ProxyList

class SuspendProxyList<S, T>(
    private val innerList: SuspendMutableList<S>,
    private val converter: BiConverter<S, T>
) : SuspendMutableList<T> {

    private fun S.toT() = converter.convertToB(this)
    private fun T.toS() = converter.convertToA(this)

    override suspend fun size(): Int = innerList.size()

    override suspend fun clear() {
        innerList.clear()
    }

    override suspend fun setAll(c: Collection<T>) {
        innerList.setAll(c.map { it.toS() })
    }

    override suspend fun addAll(elements: SuspendCollection<T>): Boolean = innerList.addAll(elements.map { it.toS() })

    override suspend fun addAll(
        index: Int,
        elements: SuspendCollection<T>
    ): Boolean = innerList.addAll(index, elements.map { it.toS() })

    override suspend fun add(
        index: Int,
        element: T
    ) = innerList.add(index, element.toS())

    override suspend fun add(element: T): Boolean = innerList.add(element.toS())

    override suspend fun get(index: Int): T = innerList.get(index).toT()

    override suspend fun isEmpty(): Boolean = innerList.isEmpty()

    @YouTrackIssue(KT, 65555)
    object ToUnCommentInK2Beta5
    override suspend fun toNonSuspendCollection(): MutableList<T> {
        val debugDelegate = ProxyList(innerList.toNonSuspendCollection(), converter)
        return object: MutableList<T> {




            override val size: Int
                get() = debugDelegate.size

            override fun clear() {
                debugDelegate.clear()
            }

            override fun addAll(elements: Collection<T>): Boolean = debugDelegate.addAll(elements)

            override fun addAll(
                index: Int,
                elements: Collection<T>
            ): Boolean = debugDelegate.addAll(index, elements)

            override fun add(
                index: Int,
                element: T
            ) {
                debugDelegate.add(index, element)
            }

            override fun add(element: T): Boolean = debugDelegate.add(element)

            override fun get(index: Int): T = debugDelegate.get(index)

            override fun isEmpty(): Boolean = debugDelegate.isEmpty()

            override fun iterator(): MutableIterator<T> = debugDelegate.iterator()

            override fun listIterator(): MutableListIterator<T> = debugDelegate.listIterator()

            override fun listIterator(index: Int): MutableListIterator<T> = debugDelegate.listIterator(index)

            override fun removeAt(index: Int): T = debugDelegate.removeAt(index)

            override fun subList(
                fromIndex: Int,
                toIndex: Int
            ): MutableList<T> = debugDelegate.subList(fromIndex, toIndex)

            override fun set(
                index: Int,
                element: T
            ): T = debugDelegate.set(index, element)

            override fun retainAll(elements: Collection<T>): Boolean = debugDelegate.retainAll(elements)

            override fun removeAll(elements: Collection<T>): Boolean = debugDelegate.removeAll(elements)

            override fun remove(element: T): Boolean = debugDelegate.remove(element)

            override fun lastIndexOf(element: T): Int = debugDelegate.lastIndexOf(element)

            override fun indexOf(element: T): Int = debugDelegate.indexOf(element)

            override fun containsAll(elements: Collection<T>): Boolean = debugDelegate.containsAll(elements)

            override fun contains(element: T): Boolean = debugDelegate.contains(element)
        }
    }



    override suspend fun iterator() = listIterator()

    override suspend fun listIterator() = listIterator(0)

    override suspend fun listIterator(index: Int) =
        run {
            val itr = innerList.listIterator(index)
            object : SuspendMutableListIterator<T> {


                override suspend fun add(element: T) {
                    itr.add(element.toS())
                }

                override suspend fun hasNext(): Boolean = itr.hasNext()

                override suspend fun hasPrevious(): Boolean = itr.hasPrevious()

                override suspend fun next(): T = itr.next().toT()

                override fun toNonSuspendingIterator(): Iterator<T> {
                    TODO()
                }

                override suspend fun nextIndex(): Int = itr.nextIndex()

                override suspend fun previous(): T = itr.previous().toT()

                override suspend fun previousIndex(): Int = itr.previousIndex()

                override suspend fun remove() = itr.remove()

                override suspend fun set(element: T) = itr.set(element.toS())
            }
        }

    override suspend fun removeAt(index: Int): T = innerList.removeAt(index).toT()

    override suspend fun subList(
        fromIndexInclusive: Int,
        toIndexExclusive: Int
    ): SuspendMutableList<T> = innerList.subList(fromIndexInclusive, toIndexExclusive).map { it.toT() }.toSuspendingFakeMutableList()

    override suspend fun set(
        index: Int,
        element: T
    ): T = innerList.set(index, element.toS()).toT()

    override suspend fun retainAll(elements: SuspendCollection<T>): Boolean = innerList.retainAll(elements.map { it.toS() })

    override suspend fun removeAll(elements: SuspendCollection<T>): Boolean = innerList.removeAll(elements.map { it.toS() })

    override suspend fun remove(element: T): Boolean = innerList.remove(element.toS())

    override suspend fun lastIndexOf(element: T): Int = innerList.lastIndexOf(element.toS())

    override suspend fun indexOf(element: T): Int = innerList.indexOf(element.toS())

    override suspend fun containsAll(elements: SuspendCollection<T>): Boolean = innerList.containsAll(elements.map { it.toS() })

    override suspend fun contains(element: T): Boolean = innerList.contains(element.toS())
}
