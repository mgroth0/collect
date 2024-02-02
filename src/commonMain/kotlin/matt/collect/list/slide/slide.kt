package matt.collect.list.slide

import matt.collect.list.linked.MyLinkedList
import matt.lang.assertions.require.requireNotEqual
import matt.lang.idea.Idea
import kotlin.math.floor

interface SlidingWindowIdea : Idea

interface SlidingWindow<E : Any> : SlidingWindowIdea {
    val centerElement: E
    val window: List<E>
}

private class MutableSlidingWindow<E : Any>(
    override var centerElement: E,
    override var window: List<E>,
) : SlidingWindow<E>

class SlidingWindowIterator<E : Any>(
    private val source: List<E>,
    private val windowSize: Int,
) : SlidingWindowIdea, Iterator<SlidingWindow<E>> {
    init {
        requireNotEqual(windowSize % 2, 0) {
            "needs to be odd to get center"
        }
    }

    private val sourceItr = source.iterator()
    private val window = MyLinkedList<E>()
    private var slidingWindow: MutableSlidingWindow<E>? = null
    private val centerI = floor(windowSize / 2.0).toInt()

    override fun hasNext(): Boolean {
        if (source.size < windowSize) return false
        if (window.isEmpty()) {
            return true
        }
        return sourceItr.hasNext()
    }

    override fun next(): SlidingWindow<E> {
        if (window.isEmpty()) {
            var center: E? = null
            repeat(windowSize) { i ->
                val n = sourceItr.next()
                if (i == centerI) {
                    center = n
                }
                window.add(n)
            }
            slidingWindow =
                MutableSlidingWindow(
                    centerElement =
                        center
                            ?: error(
                                "null center (window.size=${window.size},centerI=$centerI,source.size=${source.size},windowSize=$windowSize)",
                            ),
                    window = window,
                )
            return slidingWindow!!
        } else {
            val n = sourceItr.next()
            window.removeFirst()
            window.add(n)
            slidingWindow!!.centerElement = window[centerI]
            return slidingWindow!!
        }
    }
}

fun <E : Any> List<E>.slide(window: Int): Sequence<SlidingWindow<E>> {
    val itr = SlidingWindowIterator(this, window)
    return itr.asSequence()
}
