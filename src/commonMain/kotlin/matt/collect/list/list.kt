package matt.collect.list

fun <E> List<E>.readOnly() = ReadOnlyList(this)
class ReadOnlyList<E>(private val list: List<E>): List<E> by list


fun <E> List<E>.phase(newStartIndex: Int) = (newStartIndex until newStartIndex.size).map {
  this[it]
} + (0 until newStartIndex).map {
  this[it]
}