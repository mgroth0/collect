package matt.collect.tree

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind.CLASS
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.CompositeDecoder.Companion.UNKNOWN_NAME
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import matt.lang.go
import matt.lang.model.value.Value
import matt.prim.str.mybuild.api.string
import matt.prim.str.requireIsOneLine
import matt.prim.str.times

// See [[ListLikeDescriptor]]
@ExperimentalSerializationApi
class TreeNodeSerialDescriptor(private val elementDescriptor: SerialDescriptor) : SerialDescriptor {
    override val elementsCount: Int = 2

    override val kind: SerialKind = CLASS

    override val serialName: String = "TreeDataNode"

    private fun badIndexError(index: Int): Nothing = error("bad index: $index")

    override fun getElementAnnotations(index: Int): List<Annotation> = when (index) {
        0 -> listOf()
        1 -> listOf()
        else -> badIndexError(index)
    }

    val chilrenDescriptor by lazy {
        listSerialDescriptor(TreeNodeSerialDescriptor(elementDescriptor))
    }

    override fun getElementDescriptor(index: Int): SerialDescriptor = when (index) {
        0 -> elementDescriptor
        1 -> chilrenDescriptor
        else -> badIndexError(index)
    }

    override fun getElementIndex(name: String): Int = when (name) {
        "value" -> 0
        "children" -> 1
        else -> error("bad name: $name")
    }

    override fun getElementName(index: Int): String = when (index) {
        0 -> "value"
        1 -> "children"
        else -> badIndexError(index)
    }

    override fun isElementOptional(index: Int): Boolean = when (index) {
        0 -> false
        1 -> true
        else -> badIndexError(index)
    }
}

@OptIn(ExperimentalSerializationApi::class)
abstract class TreeMaybeDataNodeSerializer<T, N : TreeMaybeDataNodeInter<T, N>>(private val elementSerializer: KSerializer<T>) :
    KSerializer<N> {
    final override val descriptor by lazy {
        TreeNodeSerialDescriptor(elementSerializer.descriptor)
    }

    final override fun deserialize(decoder: Decoder): N = decoder.decodeStructure(descriptor) {
        var theValue: Value<T>? = null
        var children: Value<List<N>>? = null
        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> {
                    val e = decodeSerializableElement(descriptor, index, elementSerializer)
                    theValue = Value(e)
                }

                1 -> {
                    val c =
                        decodeSerializableElement(
                            descriptor,
                            index,
                            ListSerializer(this@TreeMaybeDataNodeSerializer),
                        )
                    children = Value(c)
                }

                DECODE_DONE -> break
                UNKNOWN_NAME -> error("unknown name?")

                else -> error("Unexpected index: $index")
            }
        }

        requireNotNull(theValue)

        construct(value = theValue.value, children = children?.value)
    }

    abstract fun construct(
        value: T,
        children: List<N>?,
    ): N

    final override fun serialize(
        encoder: Encoder,
        value: N,
    ) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, elementSerializer, value.value)
            value.children?.go { children ->
                encodeSerializableElement(
                    descriptor,
                    1,
                    ListSerializer(this@TreeMaybeDataNodeSerializer),
                    children,
                )
            }
        }
    }
}

class TreeDataNodeSerializer<T>(elementSerializer: KSerializer<T>) :
    TreeMaybeDataNodeSerializer<T, TreeDataNode<T>>(elementSerializer) {
    override fun construct(
        value: T,
        children: List<TreeDataNode<T>>?,
    ): TreeDataNode<T> = TreeDataNode(value, children)
}

class TreeNodeSerializer<T>(elementSerializer: KSerializer<T>) :
    TreeMaybeDataNodeSerializer<T, TreeNode<T>>(elementSerializer) {
    override fun construct(
        value: T,
        children: List<TreeNode<T>>?,
    ): TreeNode<T> = TreeNode(value, children)
}

sealed interface TreeMaybeDataNodeInter<T, N : TreeMaybeDataNodeInter<T, N>> {
    val value: T
    val children: List<N>?

    fun construct(
        value: T,
        children: List<N>,
    ): N
}

interface TreeNodeInter<T, N : TreeNodeInter<T, N>> : TreeMaybeDataNodeInter<T, N>

interface TreeDataNodeInter<T, N : TreeDataNodeInter<T, N>> : TreeMaybeDataNodeInter<T, N>

abstract class TreeDataNodeBase<T, N : TreeDataNodeBase<T, N>> : TreeDataNodeInter<T, N> {
    final override fun equals(other: Any?): Boolean = other is TreeDataNodeInter<*, *> &&
        other.value == value &&
        other.children == children

    final override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + (children?.hashCode() ?: 0)
        return result
    }

    final override fun toString(): String = "${this::class.simpleName}[value=$value,size=${children?.size}]"
}

abstract class TreeNodeBase<T, N : TreeNodeBase<T, N>> : TreeNodeInter<T, N> {
    final override fun toString(): String = "${this::class.simpleName}[value=$value,size=${children?.size}]"
}

@Serializable(with = TreeDataNodeSerializer::class)
class TreeDataNode<T>(
    override val value: T,
    override val children: List<TreeDataNode<T>>? = null,
) : TreeDataNodeBase<T, TreeDataNode<T>>() {
    override fun construct(
        value: T,
        children: List<TreeDataNode<T>>,
    ): TreeDataNode<T> = TreeDataNode(value, children)
}

@Serializable(with = TreeNodeSerializer::class)
class TreeNode<T>(
    override val value: T,
    override val children: List<TreeNode<T>>? = null,
) : TreeNodeBase<T, TreeNode<T>>() {
    override fun construct(
        value: T,
        children: List<TreeNode<T>>,
    ): TreeNode<T> = TreeNode(value, children)
}

class BiTreeNode<T>(
    val parents: Set<BiTreeNode<T>>,
    override val value: T,
    override val children: List<BiTreeNode<T>>? = null,
) : TreeNodeBase<T, BiTreeNode<T>>() {
    override fun construct(
        value: T,
        children: List<BiTreeNode<T>>,
    ): BiTreeNode<T> {
        error("can't construct BiTreeNode this way... would lose parent info")
        // return BiTreeNode(parent = null, value, children)
    }
}

class MutableTreeDataNode<T>(
    override val value: T,
    override val children: MutableList<MutableTreeDataNode<T>> = mutableListOf(),
) : TreeDataNodeBase<T, MutableTreeDataNode<T>>() {
    override fun construct(
        value: T,
        children: List<MutableTreeDataNode<T>>,
    ): MutableTreeDataNode<T> = MutableTreeDataNode(value, children.toMutableList())
}

class MutableTreeNode<T>(
    override val value: T,
    override val children: MutableList<MutableTreeNode<T>> = mutableListOf(),
) : TreeNodeBase<T, MutableTreeNode<T>>() {
    override fun construct(
        value: T,
        children: List<MutableTreeNode<T>>,
    ): MutableTreeNode<T> = MutableTreeNode(value, children.toMutableList())
}

fun <T, R> TreeDataNode<T>.map(op: (T) -> R): TreeDataNode<R> {
    val newValue: R = op(value)
    val newChildren: List<TreeDataNode<R>>? =
        children?.map {
            it.map(op)
        }
    return TreeDataNode(newValue, newChildren)
}

fun <T, R> MutableTreeDataNode<T>.map(op: (T) -> R): MutableTreeDataNode<R> {
    val newValue: R = op(value)
    val newChildren: List<MutableTreeDataNode<R>> =
        children.map {
            it.map(op)
        }
    return MutableTreeDataNode(newValue, newChildren.toMutableList())
}

// cannot do this with tree data node!! the hashCode will not work!
fun <T, R, N : TreeNodeInter<T, N>> N.mapCircular(
    mapped: MutableMap<TreeNodeInter<*, *>, MutableTreeNode<R>> = mutableMapOf(),
    op: (T) -> R,
): MutableTreeNode<R> = mapped[this] ?: run {
    val newValue: R = op(value)
    val newNode = MutableTreeNode(newValue)
    mapped[this] = newNode

    children?.forEach {
        newNode.children.add(it.mapCircular(mapped, op))
    }

    newNode
}

fun <T> MutableTreeNode<T>.toImmutableTreeCircular(mapped: MutableMap<MutableTreeNode<T>, TreeNode<T>> = mutableMapOf()): TreeNode<T> {
//    println("Running toImmutableTreeCircular")

    return mapped[this] ?: run {
        val childList = mutableListOf<TreeNode<T>>()
        val newNode = TreeNode(value, childList)
        mapped[this] = newNode

        children.forEach {
            childList.add(it.toImmutableTreeCircular(mapped))
        }
        newNode
    }
}

fun <T> TreeNode<T>.toBiTreeNodeCircular(
    parent: BiTreeNode<T>? = null,
    mapped: MutableMap<TreeNode<T>, BiTreeNode<T>> = mutableMapOf(),
): BiTreeNode<T> {
//    println("Running toBiTreeNodeCircular")

    return mapped[this]?.also {
        if (parent != null) {
//            println("YES ADDING SOME PARENT 1")
            (it.parents as MutableSet<BiTreeNode<T>>).add(parent)
        }
    } ?: run {
        val childList = mutableListOf<BiTreeNode<T>>()
        val newNodeParents = mutableSetOf<BiTreeNode<T>>()
//        println("YES ADDING SOME PARENT 2")
        if (parent != null) newNodeParents.add(parent)
        val newNode = BiTreeNode(newNodeParents, value, childList)
        mapped[this] = newNode

        children?.forEach {
            childList.add(it.toBiTreeNodeCircular(newNode, mapped))
        }
        newNode
    }
}

fun <T> TreeDataNode<T>.allValuesRecursive() = allValuesRecursiveSequence().toList()

fun <T> MutableTreeDataNode<T>.allValuesRecursive() = allValuesRecursiveSequence().toList()

fun <T> TreeDataNode<T>.allValuesRecursiveSequence(): Sequence<T> =
    sequence {
        yield(value)
        children?.forEach {
            yieldAll(it.allValuesRecursiveSequence())
        }
    }

fun <T> MutableTreeDataNode<T>.allValuesRecursiveSequence(): Sequence<T> =
    sequence {
        yield(value)
        children.forEach {
            yieldAll(it.allValuesRecursiveSequence())
        }
    }

// cannot do this with tree data node!! the hashCode will not work!
fun <T, N : TreeNodeInter<T, N>> N.allValuesRecursiveSequenceCircular(yielded: MutableSet<N> = mutableSetOf()): Sequence<T> =
    sequence {
        if (yielded.add(this@allValuesRecursiveSequenceCircular)) {
            yield(value)
            children?.forEach {
                yieldAll(it.allValuesRecursiveSequenceCircular(yielded))
            }
        }
    }

fun TreeDataNode<*>.buildTreeString(depth: Int = 0): String = string {
    lineDelimited {
        +(("\t" * depth) + value.toString().requireIsOneLine())
        children?.takeIf { it.isNotEmpty() }?.go {
            it.forEach {
                +it.buildTreeString(depth + 1)
            }
        }
    }
}
