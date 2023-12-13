package matt.collect.list

import matt.collect.itr.duplicates
import matt.collect.itr.subList
import matt.lang.assertions.require.requireEmpty
import matt.lang.disabledCode
import matt.prim.str.elementsToString
import matt.prim.str.mybuild.api.string
import kotlin.math.max
import kotlin.math.min

abstract class StructuralList<E> : List<E> {
    companion object {
        /*taken from kotlin standard lib*/
        private fun orderedHashCode(c: Collection<*>): Int {
            var hashCode = 1
            for (e in c) {
                hashCode = 31 * hashCode + (e?.hashCode() ?: 0)
            }
            return hashCode
        }

        /*taken from kotlin standard lib*/
        private fun orderedEquals(
            c: Collection<*>,
            other: Collection<*>
        ): Boolean {
            if (c.size != other.size) return false

            val otherIterator = other.iterator()
            for (elem in c) {
                val elemOther = otherIterator.next()
                if (elem != elemOther) {
                    return false
                }
            }
            return true
        }
    }

    /*taken from kotlin standard lib*/
    final override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is List<*>) return false

        return orderedEquals(this, other)
    }

    /*taken from kotlin standard lib*/
    final override fun hashCode(): Int {
        return orderedHashCode(this)
    }
}

fun <E : Any> List<E>.nullable(): List<E?> = map<E, E?> { it }
fun <E> List<E>.readOnly() = ReadOnlyList(this)

inline fun <E, reified R : E> List<E>.requireEachIs(): List<R> = map { it as R }

class ReadOnlyList<E>(private val list: List<E>) : StructuralList<E>(), List<E> by list


fun <E> List<E>.phase(newStartIndex: Int) = (newStartIndex..<size).map {
    this[it]
} + (0..<newStartIndex).map {
    this[it]
}


fun <E> List<E>.downSampled() = slice(indices step 10)


fun <E> MutableList<E>.swapWithNoDuplications(
    itemOne: E,
    itemTwo: E
) {
    swapWithNoDuplications(indexOf(itemOne), indexOf(itemTwo))
}

fun <E> MutableList<E>.swapWithNoDuplications(
    indexOne: Int,
    indexTwo: Int
) {
    if (indexOne == indexTwo) return
    val min = min(indexOne, indexTwo)
    val max = max(indexOne, indexTwo)
    val o2 = removeAt(max)
    val o1 = removeAt(min)
    add(min, o2)
    add(max, o1)
}


fun <E> MutableList<E>.swapQuickly(
    indexOne: Int,
    indexTwo: Int
) {
    this[indexOne] = set(indexTwo, this[indexOne])
}


fun <E> MutableList<E>.setAllOneByOneNeverAllowingDuplicates(source: List<E>) {

    disabledCode {
        /*these checks are great for debugging, but extremely expensive and kill performance*/
        requireEmpty(source.duplicates()) {
            "found duplicates in ${source}: ${source.duplicates().elementsToString()}"
        }
        requireEmpty(duplicates()) {
            "found duplicate in ${this}: ${duplicates().elementsToString()}"
        }
    }

    if (isEmpty()) {
        addAll(source)
        return
    }

    val sourceItr = source.listIterator()
    val targetItr = listIterator()

    val addAfter by lazy { mutableMapOf<Int, E>() }

    while (sourceItr.hasNext()) {

        if (!targetItr.hasNext()) {
            addAll(source.subList(sourceItr.nextIndex()))
            return
        }

        val sourceNext = sourceItr.next()
        val targetNext = targetItr.next()

        if (targetNext != sourceNext) {

            val targetIdxOfSourceNext = indexOf(sourceNext)

            if (targetIdxOfSourceNext >= 0) {

                targetItr.remove()
                addAfter[sourceItr.previousIndex()] = sourceNext


            } else {
                targetItr.set(sourceNext)
            }


        }


    }

    while (targetItr.hasNext()) {
        targetItr.next()
        targetItr.remove()
    }

    addAfter.entries.forEach {
        add(it.key, it.value)
    }

}


fun <E> List<E>.diffWithTarget(target: List<E>) = ListDiff(targetList = target, testList = this)

interface Diff

class ListDiff<E>(
    val targetList: List<E>,
    val testList: List<E>
): Diff {
    val isSameSize = targetList.size == testList.size

    val needToAdd = targetList.mapIndexedNotNull { index, e ->
        if (index !in testList.indices || testList[index] != e) {
            IndexedValue(index = index, value = e)
        } else null
    }
    val needToRemove = testList.mapIndexedNotNull { index, e ->
        if (index !in targetList.indices || targetList[index] != e) {
            IndexedValue(index = index, value = e)
        } else null
    }

    override fun toString(): String {
        return string {
            lineDelimited {
                +"To Add"
                needToAdd.forEach { +"\t$it" }
                blankLine()
                +"To Remove"
                needToRemove.forEach {
                    +"\t$it"
                }
            }
        }
    }
}


inline fun <reified R> List<*>.castToList(): List<R> {
    val itr = iterator()
    return List(size) {
        itr.next() as R
    }
}