package matt.collect.map.diff

import matt.collect.list.Diff
import matt.prim.str.mybuild.api.string
import matt.prim.str.mybuild.extensions.indent

infix fun <K : Any, V : Any> Map<K, V>.diff(other: Map<K, V>): MapDiff {
    val mutableThis = toMutableMap()

    val mutableOther = other.toMutableMap()

    mutableThis.entries.toList().forEach {
        if (other[it.key] == it.value) {
            mutableThis.remove(it.key)
            mutableOther.remove(it.key)
        }
    }

    val missingFrom1 = mutableOther.entries.filter { it.key !in mutableThis }.map { it.toImmutableEntry() }
    val missingFrom2 = mutableThis.entries.filter { it.key !in mutableOther }.map { it.toImmutableEntry() }
    val differentValues = mutableThis.entries.filter { it.key in mutableOther }.map { it.key }

    return MapDiff(
        missingFrom1 = missingFrom1,
        missingFrom2 = missingFrom2,
        differentValues =
            differentValues.map {
                KeyWithDifferentValues(key = it, value1 = mutableThis[it]!!, value2 = mutableOther[it]!!)
            }
    )
}

fun <K, V> Map.Entry<K, V>.toImmutableEntry() = ImmutableEntry(key = key, value = value)

class ImmutableEntry<K, V>(
    override val key: K,
    override val value: V
) : Map.Entry<K, V>

class KeyWithDifferentValues<K, V>(
    val key: K,
    val value1: V,
    val value2: V
)

class MapDiff(
    val missingFrom1: List<ImmutableEntry<*, *>>,
    val missingFrom2: List<ImmutableEntry<*, *>>,
    val differentValues: List<KeyWithDifferentValues<*, *>>
) : Diff {
    val report by lazy {
        string {

            columned {
                row("Missing From 1")
                indent {
                    missingFrom1.forEach {
                        row(it.key, it.value)
                    }
                }
                row("Missing From 2")
                indent {
                    missingFrom2.forEach {
                        row(it.key, it.value)
                    }
                }
                row("Different Values")
                indent {
                    differentValues.forEach {
                        row(it.key, it.value1, it.value2)
                    }
                }
            }
        }
    }
}
