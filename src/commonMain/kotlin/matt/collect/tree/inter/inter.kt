package matt.collect.tree.inter

interface TreeValue<T, N: TreeValue<T, N>>: TreeLike<N> {
    val value: T
}

interface TreeLike<N : TreeLike<N>> {
    val children: Set<N>
}

interface WebLike<N : WebLike<N>> {
    val connections: Set<N>
}


interface Graph<V>
