package matt.collect.tree.impl.linear.bi

import matt.collect.itr.recurse.recurse
import matt.collect.tree.ext.recursiveChildrenSeq
import matt.collect.tree.inter.TreeValue


class LinearBiTreeNode<T>(
    lazyParent: Lazy<LinearBiTreeNode<T>>?, /*lazy so it can be safely constructed*/
    override val value: T,
    childs: Iterable<LinearBiTreeNode<T>>
) : TreeValue<T, LinearBiTreeNode<T>> {
    override val children = childs.toSet() /*guarantee immutability*/

    val parent by lazy {
        val v = lazyParent?.value
        check(v != this) /*guarantee linear*/
        recursiveChildrenSeq().forEach {
            check(it != v) /*guarantee linear*/
        }
        v
    }

    init {
        children.forEach { child ->
            check(child != this) /*guarantee linear*/
            child.recurse(includeSelf = false) { it.children }.forEach {
                check(it != this) /*guarantee linear*/
                check(it !in children) /*guarantee no repeated references*/
            }
        }
    }
}



