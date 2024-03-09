package matt.collect.tree.impl.graph.visitor

import matt.collect.itr.nextOrNull
import matt.collect.tree.impl.graph.edge.Edge
import matt.collect.tree.impl.graph.path.Path
import matt.collect.tree.impl.graph.vertex.Vertex
import matt.lang.atomic.AtomicBool
import kotlin.jvm.JvmInline


open class Visitor<V>(
    private val start: Vertex<V>,
    private val assumeAcyclic: Boolean
) {
    private var location: Vertex<V> = start
    private val graph = location.graph
    private var path = Path(graph)
    private val visits = mutableListOf(Visit(location.edges.iterator()))
    private val visited = mutableSetOf(location.value)

    private var stopped = false
    protected fun stop() {
        stopped = true
    }

    open fun visit(value: V) {}


    protected val ran = AtomicBool(false)
    fun run() {
        if (ran.getAndSet(true)) error("can only run once")
        while (true) {
            if (stopped) break
            when (val advanceResult = tryToAdvance(skipVisited = true)) {
                is AdvancedTo -> visit(advanceResult.value)
                is SkipVisited -> Unit
                CanNotAdvance -> {
                    if (stopped) break
                    when (goBack()) {
                        is WentBackTo -> Unit
                        CanNotGoBack  -> break
                    }
                }
            }
            if (stopped) break
        }
    }


    private fun goBack(): GobackResult<V> {
        if (path.isEmpty) return CanNotGoBack
        visits.removeLast()
        path = path.withLastRemoved()
        location = if (path.isEmpty) start else graph.vertex(path.end)
        return WentBackTo(location.value)
    }


    private fun tryToAdvance(
        skipVisited: Boolean
    ): AdvanceResult<V> {
        val thisVisit = visits.last()
        val itr = thisVisit.itr
        val nextEdge = itr.nextOrNull() ?: return CanNotAdvance
        val nextValue = nextEdge.to
        if (
            !assumeAcyclic
            && skipVisited
            && nextValue in visited
        ) {
            return SkipVisited(nextValue)
        }
        path += nextEdge
        location = graph.vertex(nextValue)
        visits.add(Visit(location.edges.iterator()))
        if (!assumeAcyclic) {
            visited.add(nextValue)
        }
        return AdvancedTo(nextValue)
    }

    private inner class Visit(val itr: Iterator<Edge<V>>)
}

sealed interface GobackResult<out V>
data object CanNotGoBack: GobackResult<Nothing>
@JvmInline
value class WentBackTo<V>(val value: V): GobackResult<V>

sealed interface AdvanceResult<out V>
data object CanNotAdvance: AdvanceResult<Nothing>
@JvmInline
value class SkipVisited<V>(val value: V): AdvanceResult<V>
@JvmInline
value class AdvancedTo<V>(val value: V): AdvanceResult<V>
