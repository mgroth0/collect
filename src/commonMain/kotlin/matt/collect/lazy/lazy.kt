package matt.collect.lazy

fun <E> Sequence<E>.toLazyList() = LazyList(this)

class LazyList<E>(sequence: Sequence<E>): List<E> {

  private val itr = sequence.iterator()
  private val list = mutableListOf<E>()

  private fun take() = itr.takeIf { it.hasNext() }?.next()?.also { list.add(it) }
  private fun takeAll() {
	do {
	  val n = take()
	} while (n != null)
  }

  override val size: Int
	get() {
	  takeAll()
	  return list.size
	}

  private val currentMaxIndex get() = list.size - 1

  override fun get(index: Int): E {
	require(index >= 0)
	while (index > currentMaxIndex) {
	  take() ?: break
	}
	return list[index]
  }

  override fun isEmpty(): Boolean {
	if (currentMaxIndex > -1) return false
	take()
	return list.isEmpty()
  }

  override fun iterator(): Iterator<E> = listIterator()
  override fun listIterator() = listIterator(0)
  override fun listIterator(index: Int) = object: ListIterator<E> {

	init {
	  require(index >= 0)
	}

	private var idx = index

	override fun hasNext(): Boolean {

	  val ni = nextIndex()

	  while (ni > currentMaxIndex) {
		take() ?: break
	  }

	  return (currentMaxIndex >= idx)

	}

	override fun hasPrevious(): Boolean {

	  val pi = previousIndex()

	  if (pi < 0) return false

	  while (pi > currentMaxIndex) {
		take() ?: break
	  }

	  return (currentMaxIndex >= idx)


	}

	override fun next(): E {
	  val r = get(nextIndex())
	  idx += 1
	  return r
	}

	override fun nextIndex() = idx

	override fun previous(): E {
	  val r = get(previousIndex())
	  idx -= 1
	  return r
	}

	override fun previousIndex() = idx - 1

  }

  override fun subList(fromIndex: Int, toIndex: Int): List<E> {
	require(fromIndex >= 0)
	require(toIndex >= 0)
	require(toIndex > fromIndex)
	if (toIndex == 0) return emptyList()
	get(toIndex - 1)
	return list.toList().subList(fromIndex, toIndex)
  }

  override fun lastIndexOf(element: E): Int {
	takeAll()
	return list.lastIndexOf(element)
  }

  override fun indexOf(element: E): Int {
	return list.indexOf(element).takeIf { it != -1 } ?: run {
	  do {
		val t = take()
	  } while (t != element && t != null)
	  list.indexOf(element)
	}
  }

  override fun containsAll(elements: Collection<E>) = elements.all { contains(it) }

  override fun contains(element: E): Boolean {
	return list.contains(element).takeIf { it } ?: run {
	  do {
		val t = take()
	  } while (t != element && t != null)
	  list.contains(element)
	}
  }

}