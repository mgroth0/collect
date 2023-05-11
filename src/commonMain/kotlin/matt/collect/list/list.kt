package matt.collect.list

import matt.collect.itr.duplicates
import matt.lang.disabledCode
import matt.prim.str.elementsToString
import matt.prim.str.mybuild.string
import kotlin.math.max
import kotlin.math.min

fun <E: Any> List<E>.nullable(): List<E?> = map<E, E?> { it }
fun <E> List<E>.readOnly() = ReadOnlyList(this)

inline fun <E, reified R: E> List<E>.requireEachIs(): List<R> = map { it as R }

class ReadOnlyList<E>(private val list: List<E>): List<E> by list


@OptIn(ExperimentalStdlibApi::class) fun <E> List<E>.phase(newStartIndex: Int) = (newStartIndex..<size).map {
  this[it]
} + (0..<newStartIndex).map {
  this[it]
}


fun <E> List<E>.downSampled() = slice(indices step 10)


fun <E> MutableList<E>.swapWithNoDuplications(itemOne: E, itemTwo: E) {
  swapWithNoDuplications(indexOf(itemOne), indexOf(itemTwo))
}

fun <E> MutableList<E>.swapWithNoDuplications(indexOne: Int, indexTwo: Int) {
  if (indexOne == indexTwo) return
  val min = min(indexOne, indexTwo)
  val max = max(indexOne, indexTwo)
  val o2 = removeAt(max)
  val o1 = removeAt(min)
  add(min, o2)
  add(max, o1)
}


fun <E> MutableList<E>.setAllOneByOneNeverAllowingDuplicates(source: List<E>) {

  /*
	require(size == toSet().size) {
	  "size=$size,toSet().size=${toSet().size}"
	}
	require(source.toSet().size == source.size) {
	  "source.toSet().size=${source.toSet().size},source.size=${source.size}"
	}
  */

  disabledCode {
	/*these checks are great for debugging, but extremely expensive and kill performance*/
	require(source.duplicates().isEmpty()) {
	  "found duplicates in ${source}: ${source.duplicates().elementsToString()}"
	}
	require(duplicates().isEmpty()) {
	  "found duplicate in ${this}: ${duplicates().elementsToString()}"
	}
  }


  val sourceItr = source.listIterator()
  val targetItr = listIterator()

  val addAfter = mutableMapOf<Int, E>()
  while (sourceItr.hasNext()) {

	val sourceNext = sourceItr.next()

	if (!targetItr.hasNext()) {
	  targetItr.add(sourceNext)
	  while (sourceItr.hasNext()) {
		targetItr.add(sourceItr.next())
	  }
	  return
	}

	val targetNext = targetItr.next()

	//	println("targetNext=$targetNext")
	//	println("sourceNext=$sourceNext")

	if (targetNext != sourceNext) {

	  //	  println("setAll 1")

	  val targetIdxOfSourceNext = indexOf(sourceNext)

	  if (targetIdxOfSourceNext >= 0) {
		//		println("setAll 2")

		targetItr.remove()
		addAfter[sourceItr.previousIndex()] = sourceNext


	  } else {
		targetItr.set(sourceNext)
	  }


	}


  }

  while (targetItr.hasNext()) {
	//	println("setAll 3")
	targetItr.next()
	targetItr.remove()
  }

  addAfter.entries.forEach {
	//	println("setAll 4")
	add(it.key, it.value)
  }

}



fun <E> List<E>.diffWithTarget(target: List<E>) = ListDiff(targetList = target, testList = this)

class ListDiff<E>(
	val targetList: List<E>,
	val testList: List<E>
) {
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
				needToAdd.forEach {
					+"\t$it"
				}
				blankLine()
				+"To Remove"
				needToRemove.forEach {
					+"\t$it"
				}
			}
		}
	}
}