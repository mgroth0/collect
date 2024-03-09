package matt.collect.tree.impl.graph.vertex

import matt.collect.mapToSet
import matt.collect.tree.impl.graph.GraphBase
import matt.collect.tree.impl.graph.visitor.Visitor
import matt.lang.assertions.require.requireEquals


class Vertex<V>(
    val graph: GraphBase<V>,
    val value: V
) {
    override fun equals(other: Any?): Boolean =
        other is Vertex<*>
            && other.value == value
            && other.graph == graph

    override fun hashCode(): Int = value?.hashCode() ?: 0

    val edges =  graph.edges[value] ?: emptySet()
    val successors by lazy {
        edges.mapToSet {
            graph.vertex(it.to)
        }
    }

    fun canReach(otherValue: V) = canReach(graph.vertex(otherValue))
    fun canReach(vertex: Vertex<V>): Boolean {
        requireEquals(graph, vertex.graph)
        if (vertex == this) return true
        val otherValue = vertex.value
        val args = value to otherValue
        val cache = graph.canReachCache
        val cached = cache[args]
        if (cached != null) return cached
        val visitor =
            ReachabilityVisitor(
                start = this,
                assumeAcyclic = graph.canAssumeAcyclic,
                target = otherValue
            )
        visitor.run()
        val calculated = visitor.found
        cache[args] = calculated
        return calculated
    }
    val allReachable by lazy {
        val visitor =
            AllReachableVisitor(
                start = this,
                assumeAcyclic =  graph.canAssumeAcyclic
            )
        visitor.run()
        visitor.reachable
    }
    val parents by lazy {
        graph.edgesByTo[value]?.mapToSet { graph.vertex(it.from) } ?: setOf()
    }
}


private class AllReachableVisitor<V>(
    start: Vertex<V>,
    assumeAcyclic: Boolean
): Visitor<V>(start = start, assumeAcyclic = assumeAcyclic) {
    private val mReachable = mutableSetOf<V>(start.value)
    val reachable: Set<V> get() = mReachable
    override fun visit(value: V) {
        mReachable.add(value)
    }
}


private class ReachabilityVisitor<V>(
    start: Vertex<V>,
    assumeAcyclic: Boolean,
    private val target: V
): Visitor<V>(start = start, assumeAcyclic = assumeAcyclic) {
    var found = false
        get() {
            check(ran.get())
            return field
        }
        private set
    override fun visit(value: V) {
        if (value == target) {
            found = true
            stop()
        }
    }
}
