package matt.collect.tree.impl.linear.data

import kotlinx.serialization.Serializable
import matt.collect.itr.recurse.recurse
import matt.collect.tree.inter.TreeValue
import matt.collect.tree.serializer.TreeDataNodeSerializer

@Serializable(TreeDataNodeSerializer::class)
class LinearTreeDataNode<T>(
    override val value: T,
    childs: Iterable<LinearTreeDataNode<T>>
) : TreeValue<T, LinearTreeDataNode<T>> {

    override fun equals(other: Any?): Boolean =
        other is LinearTreeDataNode<*> &&
            other.value == value

    override fun toString(): String = "${this::class.simpleName}[value=$value,size=${children?.size}]"
    override fun hashCode(): Int = value?.hashCode() ?: 0

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


