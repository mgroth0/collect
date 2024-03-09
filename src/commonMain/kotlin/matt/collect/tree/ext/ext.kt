package matt.collect.tree.ext

import matt.collect.itr.recurse.recurse
import matt.collect.mapToSet
import matt.collect.tree.impl.linear.LinearTreeNode
import matt.collect.tree.impl.linear.bi.LinearBiTreeNode
import matt.collect.tree.impl.linear.data.LinearTreeDataNode
import matt.collect.tree.inter.TreeLike
import matt.collect.tree.inter.TreeValue
import matt.lang.common.go
import matt.prim.str.mybuild.api.string
import matt.prim.str.requireIsOneLine
import matt.prim.str.times

/* Controlled Destruction */
fun <T> LinearTreeNode<T>.toData(): LinearTreeDataNode<T> = LinearTreeDataNode(value, children.mapToSet { it.toData() })

fun <T> LinearTreeNode<T>.withParentReferences() = toLinearBiTreeNode(null)
private fun <T> LinearTreeNode<T>.toLinearBiTreeNode(
    parent: Lazy<LinearBiTreeNode<T>>?
): LinearBiTreeNode<T> {

    var converted: LinearBiTreeNode<T>? = null

    val convertedChilds =
        children.mapToSet {
            it.toLinearBiTreeNode(parent = lazy { converted!! })
        }

    converted  =
        LinearBiTreeNode(
            lazyParent = parent,
            value = value,
            childs = convertedChilds
        )

    return converted
}


fun <E, R> LinearTreeNode<E>.map(op: (E) -> R): LinearTreeNode<R> {
    val mappedChildren =
        children.mapToSet {
            it.map(op)
        }
    val mappedValue = op(value)
    return LinearTreeNode(mappedValue, mappedChildren)
}

/*
    Tree shape could change if conversions collide
*/
fun <E, R> LinearTreeDataNode<E>.map(op: (E) -> R): LinearTreeDataNode<R> {
    val mappedChildren =
        children.mapToSet {
            it.map(op)
        }
    val mappedValue = op(value)
    return LinearTreeDataNode(mappedValue, mappedChildren)
}




fun <T, N: TreeValue<T, N>> N.allValuesRecursiveSequence(): Sequence<T> =
    sequence {
        yield(value)
        children.forEach {
            yieldAll(it.allValuesRecursiveSequence())
        }
    }


fun LinearTreeDataNode<*>.buildTreeString(depth: Int = 0): String =
    string {
        lineDelimited {
            +(("\t" * depth) + value.toString().requireIsOneLine())
            children.takeIf { it.isNotEmpty() }?.go {
                it.forEach {
                    +it.buildTreeString(depth + 1)
                }
            }
        }
    }


fun <T, N: TreeValue<T, N>> N.recurseValues(): Sequence<T> = recurse().map { it.value }
fun <N: TreeLike<N>> N.recurse(): Sequence<N> = recurse { it.children }
fun <N: TreeLike<N>> N.recursiveChildrenSeq(): Sequence<N> =
    children.asSequence().flatMap {
        it.recurse { it.children }
    }

fun TreeLike<*>.countThisAndChildren(): Int = 1 + children.sumOf { it.countThisAndChildren() }
