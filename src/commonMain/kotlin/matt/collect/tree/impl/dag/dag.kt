package matt.collect.tree.impl.dag

import kotlinx.serialization.Serializable
import matt.collect.itr.flatMapToSet
import matt.collect.mapToSet
import matt.collect.tree.impl.dag.ser.DagSerializer
import matt.collect.tree.impl.graph.GraphBase
import matt.collect.tree.impl.graph.edge.Edge
import matt.lang.anno.Coordinated
import matt.lang.anno.SeeURL


typealias Dag<V> = DirectedAcyclicGraph<V>

/*

Does not implement structural equality because that would make  [[Vertex#equals]] have bad performance. And forcing referential equality in there could cause behavior to not meet expectations.

*/
@SeeURL("https://www.wikiwand.com/en/Directed_acyclic_graph")
@Serializable(with = DagSerializer::class)
class DirectedAcyclicGraph<V>(
    vertices: Iterable<V>,
    edges: Iterable<Edge<V>>
): GraphBase<V>(vertices, edges) {


    internal fun compress(): Serialized<V> {

        val vertexPointers = vertices.values.withIndex().associate { it.index to it.value.value }
        val inverseVertexPointers = vertexPointers.entries.associate { it.value to it.key }
        val serializedEdges =
            edges.entries.flatMapToSet {
                val n1 = inverseVertexPointers[it.key]!!
                val n2s =
                    it.value
                        .asSequence()
                        .map { it.to }
                        .map { inverseVertexPointers[it]!! }
                n2s.mapToSet { n2 ->
                    listOf(n1, n2)
                }
            }
        return Serialized(
            vertices = vertexPointers,
            edges = serializedEdges
        )
    }

    @Coordinated(63433525)
    @Serializable
    internal data class Serialized<V>(
        val vertices: Map<Int, V>,
        val edges: Set<List<Int>>
    ) {
        fun decompress(): Dag<V> =
            Dag(
                vertices = vertices.values,
                edges = edges.asSequence().mapToSet { Edge(vertices[it[0]]!!, vertices[it[1]]!!) }
            )
    }

    override val mightBeCyclic = false



    internal fun verify() {
        vertices.values.forEach { vertex1 ->
            vertex1.successors.forEach { vertex2 ->
                check(!vertex2.canReach(vertex1)) /* validates graphic is acyclic */
            }
        }
    }
}

