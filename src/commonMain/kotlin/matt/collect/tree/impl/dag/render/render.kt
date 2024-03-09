package matt.collect.tree.impl.dag.render

import matt.collect.tree.impl.dag.Dag


fun <T> Dag<T>.renderToString(): String = "Dag with ${vertices.size} vertices and ${edges.size} edges. TASK: RENDER TO STRING"
