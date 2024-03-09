package matt.collect.tree.impl.web

import matt.collect.tree.impl.graph.GraphBase
import matt.collect.tree.impl.graph.edge.Edge


class DirectedWeb<V>(
    vertices: Iterable<V>,
    edges: Iterable<Edge<V>>
): GraphBase<V>(vertices, edges) {
    override val mightBeCyclic = true
}
