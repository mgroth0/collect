package matt.collect.tree.impl.web.builder

import matt.collect.tree.impl.graph.edge.Edge
import matt.collect.tree.impl.web.DirectedWeb


class DirectedWebBuilder<V>() {

    private val vertices = mutableSetOf<V>()
    private val edges = mutableSetOf<Edge<V>>()

    fun addVertex(value: V) {
        vertices.add(value)
    }
    fun addEdge(from: V, to: V) {
        edges += Edge(from = from, to = to)
    }

    fun build() =
        DirectedWeb<V>(
            vertices = vertices,
            edges = edges
        )
}
