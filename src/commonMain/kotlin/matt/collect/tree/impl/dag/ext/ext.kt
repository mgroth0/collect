package matt.collect.tree.impl.dag.ext

import matt.collect.list.linked.construct.mutableLinkedList
import matt.collect.mapToSet
import matt.collect.tree.impl.dag.Dag
import matt.collect.tree.impl.dag.DirectedAcyclicGraph
import matt.collect.tree.impl.graph.edge.Edge


fun <T, V> T.toDag(
    successorProvider: (T) -> Iterable<T>,
    valueProvider: (T) -> V
): DirectedAcyclicGraph<V> {


    val verts = mutableSetOf<V>()
    val edges = mutableMapOf<V, MutableSet<Edge<V>>>()

    val queue = mutableLinkedList<T>()
    queue.add(this)

    while (queue.isNotEmpty()) {
        val itr = queue.listIterator()
        while (itr.hasNext()) {
            val parent = itr.next()
            val parentValue = valueProvider(parent)
            itr.remove()
            if (parentValue !in verts) {
                val sucs = successorProvider(parent)
                sucs.forEach {
                    val sucValue = valueProvider(it)
                    edges.getOrPut(parentValue) {
                        mutableSetOf()
                    }.add(Edge(parentValue, sucValue))
                    itr.add(it)
                }
                verts.add(parentValue)
            } else {
                val sucs = successorProvider(parent).mapToSet { valueProvider(it) }
                val existingSucs = (edges[parentValue]?.mapToSet { it.to } ?: setOf())
                check(sucs.toSet() == existingSucs)
            }
        }
    }

    return DirectedAcyclicGraph(
        vertices = verts,
        edges = edges.values.flatten()
    )
}



fun <T, R> Dag<T>.map(op: (T) -> R): Dag<R> {
    val cache = mutableMapOf<T, R>()
    fun convert(t: T) = cache.getOrPut(t) { op(t) }
    return Dag(
        vertices = vertexValues.mapToSet { convert(it) },
        edges =
            edges.flatMapTo(mutableSetOf()) {
                val rKey = convert(it.key)
                it.value.mapToSet { Edge(rKey, convert(it.to)) }
            }
    )
}

fun <T> Dag<T>.mergeWith(other: Dag<T>): Dag<T> =
    Dag(
        vertices = vertexValues + other.vertexValues,
        edges = edgesSet + other.edgesSet
    )
