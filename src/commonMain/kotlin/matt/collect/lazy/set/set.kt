package matt.collect.lazy.set

import matt.collect.lazy.LazyCollection
import matt.lang.common.go
import matt.lang.sync.common.ReferenceMonitor
import matt.lang.sync.common.inSync
import matt.lang.sync.inSync

class LazySet<E : Any>(
    private val generator: Iterator<E>
) : LazyCollection<E>, Set<E>, ReferenceMonitor {
    constructor(generator: Sequence<E>) : this(generator.iterator())

    private val theSet = mutableSetOf<E>()

    private var lastGeneratedFrom: Any? = null

    private fun generateAll(from: Any) =
        inSync {
            lastGeneratedFrom = from
            while (generator.hasNext()) {
                theSet += generator.next()
            }
        }

    private fun generateAnother(from: Any): E? =
        inSync {
            lastGeneratedFrom = from
            if (generator.hasNext()) {
                val n = generator.next()
                theSet += n
                return n
            }
            return null
        }

    override val size: Int
        get() =
            inSync {
                generateAll(this)
                return theSet.size
            }

    override fun contains(element: E): Boolean =
        inSync {
            if (element in theSet) return true
            do {
                val n = generateAnother(this)
                if (n == element) return true
            } while (n != null)
            return false
        }

    override fun containsAll(elements: Collection<E>): Boolean =
        inSync {
            return elements.all { it in this }
        }

    override fun isEmpty(): Boolean =
        inSync {
            return if (theSet.isNotEmpty()) {
                false
            } else {
                generateAnother(this)
                theSet.isEmpty()
            }
        }

    override fun iterator() =
        inSync {
            object : Iterator<E> {
                init {
                    lastGeneratedFrom = null
                }

                private var itr = theSet.toSet().iterator()

                override fun hasNext(): Boolean =
                    inSync(this@LazySet) {
                        lastGeneratedFrom?.go {
                            if (it != this) {
                                error("bad lazy set")
                            }
                        }

                        if (itr.hasNext()) {
                            true
                        } else {
                            generator.hasNext()
                        }
                    }

                override fun next(): E =
                    inSync(this@LazySet) {
                        lastGeneratedFrom?.go {
                            if (it != this) {
                                error("bad lazy set")
                            }
                        }
                        if (itr.hasNext()) {
                            itr.next()
                        } else {
                            generateAnother(this)!!
                        }
                    }
            }
        }
}
