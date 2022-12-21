package matt.collect.weak

import matt.collect.dmap.withStoringDefault

fun <K, V> lazyWeakMap(op: (K)->V) = WeakMap<K, V>().withStoringDefault { op(it) }

/*SERIOUS EQUALITY ISSUES HAVE LEAD ME TO USING HASH CODES INSTEAD OF THE OBJECTS THEMSELVES*/
/*... IT WORKED*/
expect class WeakMap<K, V>(): MutableMap<K, V>


