package matt.collect.tree.impl.graph.path

import matt.collect.tree.impl.graph.edge.Edge
import matt.collect.tree.inter.Graph


class Path<V>(
    val graph: Graph<V>,
    vararg edges: Edge<V>
) {
    private val edges = edges.toList()
    init {
        if (edges.size > 1) {
            (1 until edges.size).forEach {
                check(edges[it].from == edges[it - 1].to)
            }
        }
    }
    val vertexValuePath by lazy {
        edges.drop(1).fold(mutableListOf(edges.first().from, edges.first().to)) { acc, edge ->
            acc.add(edge.to)
            acc
        }
    }

    override fun equals(other: Any?): Boolean = other is Path<*> && other.graph == graph && other.edges == edges

    override fun hashCode(): Int {
        var result = graph.hashCode()
        result = 31 * result + edges.hashCode()
        return result
    }

    val isEmpty = edges.isEmpty()
    val edgeCount = edges.size

    operator fun plus(edge: Edge<V>): Path<V> {
        if (edges.isNotEmpty()) {
            check(edge.from == edges.last().to) {
                "Added edge must connect to end of path. ${edge.from} is not ${edges.last().to}"
            }
        }
        return Path(graph, *edges.toTypedArray(), edge)
    }

    val start by lazy {
        edges.first().from
    }
    val end by lazy {
        edges.last().to
    }

    fun withLastRemoved(): Path<V> {
        check(edgeCount > 0)
        return Path(graph, *edges.subList(0, edges.size - 1).toTypedArray())
    }
}
