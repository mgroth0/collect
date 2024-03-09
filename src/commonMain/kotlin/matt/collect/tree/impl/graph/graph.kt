package matt.collect.tree.impl.graph

import matt.collect.map.sync.synchronized
import matt.collect.tree.impl.graph.edge.Edge
import matt.collect.tree.impl.graph.vertex.Vertex
import matt.collect.tree.inter.Graph
import matt.collect.weak.lazy.lazyWeakMap


abstract class GraphBase<V>(
    vertices: Iterable<V>,
    edges: Iterable<Edge<V>>
): Graph<V> {
    val edgesSet = edges.toSet()
    val edges = edgesSet.groupBy { it.from }
    internal val edgesByTo = edgesSet.groupBy { it.to }
    val vertexValues = vertices.toSet()
    val vertices = vertexValues.associateWith { Vertex(this, it) }

    init {
        edges.forEach {
            check(it.from in vertexValues)
            check(it.to in vertexValues)
        }
    }

    internal fun vertex(value: V) = vertices[value]!!



    internal val canReachCache =
        lazyWeakMap<Pair<V, V>, Boolean> {
            vertex(it.first).canReach(it.second)
        }.synchronized()

    abstract val mightBeCyclic: Boolean
    val canAssumeAcyclic get() = !mightBeCyclic
}
