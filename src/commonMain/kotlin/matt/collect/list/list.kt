package matt.collect.list

import matt.collect.itr.duplicates
import matt.lang.disabledCode
import matt.prim.str.elementsToString
import kotlin.math.max
import kotlin.math.min

fun <E> List<E>.readOnly() = ReadOnlyList(this)
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