package matt.collect.list.array

import kotlinx.serialization.Serializable
import matt.lang.lastIndex
import kotlin.jvm.JvmInline


sealed interface ArrayWrapper<T> : List<T> {
    override operator fun get(index: Int): T
    override val size: Int


    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun iterator() = object : Iterator<T> {
        private var i = -1
        override fun hasNext(): Boolean {
            return (i + 1) <= lastIndex
        }

        override fun next(): T {
            return get(++i)
        }

    }

    override fun listIterator(): ListIterator<T> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): ListIterator<T> {
        TODO("Not yet implemented")
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): List<T> {
        TODO("Not yet implemented")
    }

    override fun lastIndexOf(element: T): Int {
        TODO("Not yet implemented")
    }

    override fun indexOf(element: T): Int {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }

}

@Serializable
@JvmInline
value class FloatArrayWrapper(private val v: FloatArray) : ArrayWrapper<Float> {
    override fun get(index: Int): Float {
        return v[index]
    }

    override val size get() = v.size


    override fun contains(element: Float): Boolean {
        v.forEach {
            if (it == element) return true
        }
        return false
    }


}

@Serializable
@JvmInline
value class DoubleArrayWrapper(private val v: DoubleArray) : ArrayWrapper<Double> {
    override fun get(index: Int): Double {
        return v[index]
    }

    override val size get() = v.size

    override fun contains(element: Double): Boolean {
        v.forEach {
            if (it == element) return true
        }
        return false
    }
}
