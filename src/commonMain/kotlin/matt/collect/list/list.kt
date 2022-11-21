package matt.collect.list

fun <E> List<E>.readOnly() = ReadOnlyList(this)
class ReadOnlyList<E>(private val list: List<E>): List<E> by list