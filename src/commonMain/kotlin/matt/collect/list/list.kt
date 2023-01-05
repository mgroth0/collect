package matt.collect.list

fun <E> List<E>.readOnly() = ReadOnlyList(this)
class ReadOnlyList<E>(private val list: List<E>): List<E> by list


fun <E> List<E>.phase(newStartIndex: Int) = (newStartIndex ..< size).map {
  this[it]
} + (0 ..< newStartIndex).map {
  this[it]
}


fun <E> List<E>.downSampled() = slice(indices step 10)