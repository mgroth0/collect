package matt.collect.tree.impl.graph.edge



data class Edge<V>(
    val from: V,
    val to: V
) {
    init {
        check(from != to)
    }
}
