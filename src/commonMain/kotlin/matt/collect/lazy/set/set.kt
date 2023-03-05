package matt.collect.lazy.set

import matt.collect.lazy.LazyCollection
import matt.lang.anno.OnlySynchronizedOnJvm
import matt.lang.go
import matt.lang.sync.inSync

class LazySet<E: Any>(
  private val generator: Iterator<E>
): LazyCollection<E>, Set<E> {

  private val theSet = mutableSetOf<E>()

  private var lastGeneratedFrom: Any? = null

  @OnlySynchronizedOnJvm private fun generateAll(
	from: Any
  ) {
	lastGeneratedFrom = from
	while (generator.hasNext()) {
	  theSet += generator.next()
	}
  }

  @OnlySynchronizedOnJvm private fun generateAnother(
	from: Any
  ): E? {
	lastGeneratedFrom = from
	if (generator.hasNext()) {
	  val n = generator.next()
	  theSet += n
	  return n
	}
	return null
  }

  override val size: Int
	@OnlySynchronizedOnJvm get() {
	  generateAll(this)
	  return theSet.size
	}

  @OnlySynchronizedOnJvm override fun contains(element: E): Boolean {
	if (element in theSet) return true
	do {
	  val n = generateAnother(this)
	  if (n == element) return true
	} while (n != null)
	return false
  }

  @OnlySynchronizedOnJvm override fun containsAll(elements: Collection<E>): Boolean {
	return elements.all { it in this }
  }

  @OnlySynchronizedOnJvm override fun isEmpty(): Boolean {
	return if (theSet.isNotEmpty()) false
	else {
	  generateAnother(this)
	  theSet.isEmpty()
	}
  }

  @OnlySynchronizedOnJvm override fun iterator() = object: Iterator<E> {


	init {
	  lastGeneratedFrom = null
	}

	private var itr = theSet.toSet().iterator()

	override fun hasNext(): Boolean {
	  return inSync(this@LazySet) {
		lastGeneratedFrom?.go {
		  if (it != this) {
			error("bad lazy set")
		  }
		}

		if (itr.hasNext()) true
		else {
		  generator.hasNext()
		}


	  }
	}

	override fun next(): E {
	  return inSync(this@LazySet) {
		lastGeneratedFrom?.go {
		  if (it != this) {
			error("bad lazy set")
		  }
		}
		if (itr.hasNext()) itr.next()
		else {
		  generateAnother(this)!!
		}
	  }
	}

  }
}