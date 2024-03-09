package matt.collect.weak.lazy

import matt.collect.map.dmap.inter.withStoringDefault
import matt.collect.weak.WeakMap

fun <K, V : Any> lazyWeakMap(op: (K) -> V) = WeakMap<K, V>().withStoringDefault { op(it) }
