package matt.collect.tree.impl.linear

import kotlinx.serialization.Serializable
import matt.collect.itr.recurse.recurse
import matt.collect.mapToSet
import matt.collect.tree.inter.TreeValue
import matt.collect.tree.serializer.TreeNodeSerializer
import matt.lang.sync.common.SimpleReferenceMonitor
import matt.lang.sync.common.inSync


class LinearTreeNodeBuilder<T>(
    val value: T
) {
    private val mChildren = mutableSetOf<LinearTreeNodeBuilder<T>>()
    private val monitor = SimpleReferenceMonitor()
    fun add(child: T) = add(LinearTreeNodeBuilder(child))
    fun add(childBuilder: LinearTreeNodeBuilder<T>) {
        check(!childBuilder.canReach(value)) {
            "Cannot add child with value ${childBuilder.value} to node with value $value, because it creates a circular path"
        } /*fail fast, prevent a stack overflow*/
        monitor.inSync {
            mChildren.add(childBuilder)
        }
    }

    private fun canReach(otherValue: T): Boolean {
        if (value == otherValue) return true
        return mChildren.any { it.canReach(otherValue) }
    }

    fun build(): LinearTreeNode<T> {
        monitor.inSync {
            return LinearTreeNode(
                value = value,
                childs = mChildren.mapToSet { it.build() }
            )
        }
    }
}


@Serializable(TreeNodeSerializer::class)
class LinearTreeNode<T>(
    override val value: T,
    childs: Iterable<LinearTreeNode<T>>
) : TreeValue<T, LinearTreeNode<T>> {
    override val children = childs.toSet() /*guarantee immutability*/
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

