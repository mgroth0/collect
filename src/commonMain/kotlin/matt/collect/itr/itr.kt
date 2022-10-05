package matt.collect.itr

import matt.collect.itr.ItrChange.Add
import matt.collect.itr.ItrChange.Remove
import matt.collect.itr.ItrChange.Replace
import matt.collect.itr.ItrDir.NEXT
import matt.collect.itr.ItrDir.PREVIOUS
import matt.lang.ILLEGAL
import matt.lang.err
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.InvocationKind.AT_LEAST_ONCE
import kotlin.contracts.InvocationKind.UNKNOWN
import kotlin.contracts.contract

fun <E> List<E>.loopIterator() = LoopIterator(this)
fun <E> MutableList<E>.loopIterator() = MutableLoopIterator(this)
fun <E> List<E>.loopListIterator() = LoopListIterator(this)
fun <E> MutableList<E>.loopListIterator() = MutableLoopListIterator(this)


fun <E> Iterator<E>.toFakeMutableIterator() = FakeMutableIterator(this)

open class FakeMutableIterator<E>(val itr: Iterator<E>): Iterator<E> by itr, MutableIterator<E> {
  override fun remove() {
	err("tried remove in ${FakeMutableIterator::class.simpleName}")
  }

}

class FakeMutableListIterator<E>(itr: ListIterator<E>): ListIterator<E> by itr,
														MutableListIterator<E> {
  override fun add(element: E) = ILLEGAL

  override fun previous() = ILLEGAL
  override fun remove() = ILLEGAL

  override fun set(element: E) = ILLEGAL


}

interface LoopIteratorFun<E>: Iterator<E> {
  val list: List<E>

  fun spin(op: ()->Unit = {}): E {
	require(hasNext())
	return (1..(1..list.size).random()).map {
	  op()
	  next()
	}.last()
  }
}


class LoopIterator<E>(override val list: List<E>): Iterator<E>, LoopIteratorFun<E> {
  private var itr = list.iterator()
  override fun hasNext() = list.isNotEmpty()

  override fun next(): E {
	require(hasNext())
	return if (itr.hasNext()) {
	  itr.next()
	} else {
	  itr = list.iterator()
	  itr.next()
	}
  }


}

class MutableLoopIterator<E>(override val list: MutableList<E>): MutableIterator<E>, LoopIteratorFun<E> {
  private var itr = list.iterator()
  override fun hasNext() = list.isNotEmpty()


  override fun next(): E {
	require(hasNext())
	return if (itr.hasNext()) {
	  itr.next()
	} else {
	  itr = list.iterator()
	  itr.next()
	}
  }

  override fun remove() {
	return itr.remove()
  }


}


class LoopListIterator<E>(override val list: List<E>): ListIterator<E>, LoopIteratorFun<E> {
  fun atEnd(): Boolean {
	require(list.isNotEmpty()) /*or else definition is ambiguous*/
	return itr.hasPrevious() && itr.previousIndex() == list.lastIndex
  }

  fun atStart(): Boolean {
	require(list.isNotEmpty()) /*or else definition is ambiguous*/
	return itr.hasNext() && itr.nextIndex() == 0
  }

  private var itr = list.listIterator()
  override fun hasNext() = list.isNotEmpty()

  override fun next(): E {
	require(hasNext())
	return if (itr.hasNext()) {
	  itr.next()
	} else {
	  itr = list.listIterator()
	  itr.next()
	}
  }

  override fun hasPrevious() = list.isNotEmpty()

  override fun nextIndex(): Int {
	require(hasNext())
	return if (itr.hasNext()) {
	  itr.nextIndex()
	} else {
	  0
	}
  }


  override fun previous(): E {
	require(hasNext())
	return if (itr.hasPrevious()) {
	  itr.previous()
	} else {
	  itr = list.listIterator(list.size)
	  itr.previous()
	}
  }

  override fun previousIndex(): Int {
	require(hasNext())
	return if (itr.hasPrevious()) {
	  itr.previousIndex()
	} else {
	  list.size - 1
	}
  }
}


class MutableLoopListIterator<E>(override val list: MutableList<E>): MutableListIterator<E>, LoopIteratorFun<E> {
  fun atEnd(): Boolean {
	require(list.isNotEmpty()) /*or else definition is ambiguous*/
	return itr.hasPrevious() && itr.previousIndex() == list.lastIndex
  }

  fun atStart(): Boolean {
	require(list.isNotEmpty()) /*or else definition is ambiguous*/
	return itr.hasNext() && itr.nextIndex() == 0
  }

  private var itr = list.listIterator()
  override fun hasNext() = list.isNotEmpty()

  override fun next(): E {
	require(hasNext())
	return if (itr.hasNext()) {
	  itr.next()
	} else {
	  endToStart()
	  itr.next()
	}
  }

  fun endToStart() {
	require(atEnd())
	itr = list.listIterator()
  }

  fun startToEnd() {
	require(atStart())
	itr = list.listIterator(list.size)
  }


  override fun hasPrevious() = list.isNotEmpty()

  override fun nextIndex(): Int {
	require(hasNext())
	return if (itr.hasNext()) {
	  itr.nextIndex()
	} else {
	  0
	}
  }

  override fun previous(): E {
	require(hasNext())
	return if (itr.hasPrevious()) {
	  itr.previous()
	} else {
	  startToEnd()
	  itr.previous()
	}
  }

  override fun previousIndex(): Int {
	require(hasNext())
	return if (itr.hasPrevious()) {
	  itr.previousIndex()
	} else {
	  list.size - 1
	}
  }

  override fun add(element: E) {
	return itr.add(element)
  }

  override fun remove() {
	return itr.remove()
  }

  override fun set(element: E) {
	return itr.set(element)
  }
}

enum class ItrDir {
  NEXT, PREVIOUS
}

open class MutableIteratorWrapper<E>(
  list: MutableCollection<E>,
  open val itrWrapper: (ItrDir, ()->E)->E = { _, it -> it() },
  val changeWrapper: (ItrChange, ()->Unit)->Unit = { _, it -> it() }
): MutableIterator<E> {

  protected open val itr = list.iterator()

  override fun hasNext() = itr.hasNext()
  override fun next() = itrWrapper(NEXT) { itr.next() }

  override fun remove(): Unit = changeWrapper(Remove) { itr.remove() }
}

enum class ItrChange {
  Add, Remove, Replace
}

open class MutableListIteratorWrapper<E>(
  list: MutableList<E>,
  index: Int? = null,
  itrWrapper: (ItrDir, ()->E)->E = { _, it -> it() },
  changeWrapper: (ItrChange, ()->Unit)->Unit = { _, it -> it() }
): MutableIteratorWrapper<E>(list, itrWrapper = itrWrapper, changeWrapper = changeWrapper), MutableListIterator<E> {
  final override val itr = if (index != null) list.listIterator(index) else list.listIterator()


  final override fun hasPrevious() = itr.hasPrevious()
  final override fun nextIndex() = itr.nextIndex()
  final override fun next() = itrWrapper(NEXT) {
	itr.next()
  }

  final override fun previous() = itrWrapper(PREVIOUS) {
	itr.previous()
  }

  final override fun previousIndex() = itr.previousIndex()

  override fun add(element: E) = changeWrapper(Add) {
	itr.add(element)
  }

  override fun set(element: E) = changeWrapper(Replace) { itr.set(element) }
}


open class MutableIteratorWithSomeMemory<E>(list: MutableCollection<E>): MutableIteratorWrapper<E>(list) {
  var hadFirstReturn = false
  var lastReturned: E? = null
  override val itrWrapper: (ItrDir, ()->E)->E = { _, it ->
	val r = it()
	hadFirstReturn = true
	lastReturned = r
	r
  }
}

open class MutableListIteratorWithSomeMemory<E>(list: MutableList<E>, index: Int? = null):
  MutableListIteratorWrapper<E>(
	list, index = index
  ) {
  private var hadFirstReturn = false
  var lastReturned: E? = null
  protected var currentIndex = index ?: 0
  final override val itrWrapper: (ItrDir, ()->E)->E = { dir, it ->
	val r = it()
	hadFirstReturn = true
	lastReturned = r
	when (dir) {
	  NEXT     -> currentIndex += 1
	  PREVIOUS -> currentIndex -= 1
	}
	r
  }
}


fun <T> mutableListsOf(num: Int) = (0 until num).map { mutableListOf<T>() }
fun <T> listsOf(num: Int) = (0 until num).map { listOf<T>() }

inline fun <T> Iterable<T>.first(errorMessage: String, predicate: (T)->Boolean): T {
  for (element in this) if (predicate(element)) return element
  throw NoSuchElementException("Collection contains no element matching the predicate (${errorMessage}).")
}


inline fun <E> MutableList<E>.iterateM(op: MutableListIterator<E>.(E)->Unit) {
  return listIterator().whileHasNext(op)
}

inline fun <E> List<E>.iterateL(op: ListIterator<E>.(E)->Unit) {
  return listIterator().whileHasNext(op)
}

inline fun <E> Iterable<E>.iterate(op: Iterator<E>.(E)->Unit) {
  return iterator().whileHasNext(op)
}

fun <T> Iterable<T>.filterNotIn(vararg matches: T): List<T> {
  return filterTo(ArrayList()) { it !in matches }
}

fun <T> Iterable<T>.filterNot(vararg matches: T) = filterNotIn(*matches)
fun <T> Iterable<T>.except(vararg matches: T) = filterNotIn(*matches)
fun <T> Iterable<T>.besides(vararg matches: T) = filterNotIn(*matches)


fun <T> Iterable<T>.filterIn(vararg matches: T): List<T> {
  return filterTo(ArrayList()) { it in matches }
}

@OptIn(ExperimentalContracts::class)
inline fun <E, I: Iterator<E>> I.whileHasNext(op: I.(E)->Unit) {
  contract {
	callsInPlace(op, UNKNOWN)
  }
  while (hasNext()) {
	val n = next()
	op(n)
  }
}


inline fun <T, R> Iterable<T>.mapNested(converter: (T, T)->R): List<R> {
  val r = mutableListOf<R>()
  for (element1 in this) for (element2 in this) r += converter(element1, element2)
  return r
}

//inline fun <T,R> Iterable<T>.mapNested(action: (T, T)->R): List<R> {
//  for (element1 in this) for (element2 in this) action(element1, element2)
//  listOf<Int>().map {  }
//  return mapTo(ArrayList<R>(collectionSizeOrDefault(10)), {  })
//}

fun <T> Sequence<T>.onEvery(ith: Int, action: (T)->Unit): Sequence<T> {
  return mapIndexed { index, t ->
	if (index%ith == 0) action(t)
	t
  }
}

fun <T> Sequence<T>.onEveryIndexed(ith: Int, action: (Int, T)->Unit): Sequence<T> {
  return mapIndexed { index, t ->
	if (index%ith == 0) action(index, t)
	t
  }
}

inline fun <T> Array<out T>.applyEach(action: T.()->Unit) {
  for (element in this) action.invoke(element)
}

//inline fun <T> Iterable<T>.applyEach(action: T.()->Unit) {
//  for (element in this) action.invoke(element)
//}


/*does not duplicate a pairing, even considering other orders. ie if A,B has been found, B,A will not be found*/
inline fun <T> Sequence<T>.forEachPairing(action: Pair<T, T>.()->Unit) {
  val unique = toSet().toList()
  var i = -1
  unique.forEach { a ->
	i += 1
	unique.subList(i + 1, unique.size).forEach { b ->
	  (a to b).action()
	}
  }
}

/*does not duplicate a pairing, even considering other orders. ie if A,B has been found, B,A will not be found*/
inline fun <T> Iterable<T>.forEachPairing(action: Pair<T, T>.()->Unit) {
  asSequence().forEachPairing(action)
}


fun Array<FloatArray>.flatten() = FloatArray(this.map { it.size }.sum()).also { r ->
  var i = 0
  forEach {
	it.forEach {
	  r[i] = it
	  i++
	}
  }
}

@Suppress("SimplifiableCall", "UNCHECKED_CAST") fun <T> Array<T>.filterNotNull(): List<T & Any> =
  filter { it != null } as List<T & Any>
//@Suppress("SimplifiableCall", "UNCHECKED_CAST") fun FloatArray.filterNotNull(): List<Float> = filter { it != null } as List<T & Any>

inline fun <T> Iterable<T>.firstOrErr(msg: String, predicate: (T)->Boolean): T {
  for (element in this) if (predicate(element)) return element
  err(msg)
}


fun <T> Collection<T>.only(): T {
  require(this.size == 1)
  return first()
}

fun <T> Sequence<T>.only() = iterator().only()
fun <T> Iterable<T>.only() = iterator().only()
fun <T> Iterator<T>.only(): T {
  val r = next()
  require(!hasNext())
  return r
}


fun <E> List<E>.sameContentsAnyOrder(list: List<E>): Boolean {
  if (size != list.size) return false
  val tempList = list.toMutableList()
  forEach {
	if (!tempList.remove(it)) return false
  }
  return tempList.isEmpty()
}

/*from is inclusive*/
fun <E> List<E>.subList(from: Int) = subList(from, size)

fun <E> List<E?>.filterNotNull(): List<E> = mapNotNull { it }
fun <E> Sequence<E?>.filterNotNull(): Sequence<E> = mapNotNull { it }

inline fun <T> Sequence<T>.applyEach(action: T.()->Unit) {
  for (element in this) action.invoke(element)
}

fun <T> Iterable<T>.applyEach(op: T.()->Unit) = forEach { it.apply(op) }

fun <T> Sequence<T>.onEachApply(op: T.()->Unit) = onEach { it.apply(op) }
fun <T> Iterable<T>.onEachApply(op: T.()->Unit) = onEach { it.apply(op) }


@ExperimentalContracts
fun <T: Any, R> T.search(
  getTarget: T.()->R?,
  getNext: T.()->T?
): R? {
  contract {
	callsInPlace(getTarget, AT_LEAST_ONCE)
	callsInPlace(getNext, UNKNOWN)
  }
  var next: T? = this
  do {
	next!!.getTarget()?.let {
	  return it
	}
	next = next.getNext()
  } while (next != null)
  return null
}


@ExperimentalContracts
fun <T: Any> T.searchDepth(
  getNext: T.()->T?
): Int {
  contract {
	callsInPlace(getNext, InvocationKind.AT_LEAST_ONCE)
  }
  var i = -1
  var next: T? = this
  do {
	next = next!!.getNext()
	i++
  } while (next != null)
  return i
}


fun <E, T> Collection<E>.allUnique(op: (E)->T) = map(op).allUnique()

fun <E> Collection<E>.allUnique(): Boolean {
  when (this) {
	is List<E> -> {
	  forEachIndexed { index1, t1 ->
		for (t2 in subList(index1 + 1, size)) {
		  if (t1 == t2) {
			println("t1 is t2")
			println("t1:$t1")
			println("t2:$t2")
			println("index1:$index1")
			println("indexOf(t1)=${this.indexOf(t1)}")
			println("indexOf(t2)=${this.indexOf(t2)}")
			return false
		  }
		}
	  }
	  return true
	}

	is Set<E>  -> {
	  return true
	}

	else       -> return toList().allUnique()
  }
}

fun <E> Collection<E>.allSame(): Boolean {
  if (this.size <= 1) {
	return true
  } else {
	val example = this.first()
	return this.all { it == example }
  }
}

fun <T> list(op: ListBuilder<T>.()->Unit) = ListBuilder<T>().apply(op)

class ListBuilder<T>(private val list: MutableList<T> = mutableListOf()): MutableList<T> by list {
  operator fun T.unaryPlus() {
	list += this
  }
}

inline fun <E, reified R> Iterable<E>.mapToArray(op: (E)->R) = map { op(it) }.toTypedArray()
inline fun <E, reified R> Array<E>.mapToArray(op: (E)->R) = map { op(it) }.toTypedArray()


fun <E> Collection<E>.duplicates(): List<Pair<IndexedValue<E>, IndexedValue<E>>> = when (this) {
  is Set  -> listOf()
  is List -> {
	val r = mutableListOf<Pair<IndexedValue<E>, IndexedValue<E>>>()
	val itr = listIterator()
	while (itr.hasNext()) {
	  val n = itr.next()
	  val i = itr.previousIndex()
	  forEachIndexed { index, e ->
		if (index != i && e == n) {
		  r += IndexedValue(i, n) to IndexedValue<E>(index, e)
		}
	  }
	}
	r
  }

  else    -> err("how to get duplicates of ${this}?")
}

inline fun <reified T> arrayOfNotNull(vararg elements: T?) = listOfNotNull(elements).toTypedArray()


fun <T> Iterator<T>.first(op: (T)->Boolean): T {
  while (hasNext()) {
	val n = next()
	if (op(n)) return n
  }
  throw NoSuchElementException("couldn't find one")
}


fun <T> ListIterator<T>.firstBackwards(op: (T)->Boolean): T {
  while (hasPrevious()) {
	val n = previous()
	if (op(n)) return n
  }
  throw NoSuchElementException("couldn't find one")
}

object YesIUseCollect
inline fun <T> Iterable<T>.forEachNested(action: (T, T)->Unit): Unit {
  for (element1 in this) for (element2 in this) action(element1, element2)
}

fun <T> Iterator<T>.nextOrNull() = takeIf { hasNext() }?.next()

