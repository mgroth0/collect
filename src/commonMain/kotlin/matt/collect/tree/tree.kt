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
import matt.collect.list.readOnly
import matt.lang.go
import matt.lang.model.value.Value

/*See [[ListLikeDescriptor]] */
@ExperimentalSerializationApi
class TreeNodeSerialDescriptor(private val elementDescriptor: SerialDescriptor) : SerialDescriptor {


    override val elementsCount: Int = 2

    override val kind: SerialKind = CLASS

    override val serialName: String = "TreeDataNode"

    override fun getElementAnnotations(index: Int): List<Annotation> {
        return when (index) {
            0    -> listOf()
            1    -> listOf()
            else -> error("bad index: $index")
        }
    }

    val chilrenDescriptor by lazy {
        listSerialDescriptor(TreeNodeSerialDescriptor(elementDescriptor))
    }

    override fun getElementDescriptor(index: Int): SerialDescriptor {
        return when (index) {
            0    -> elementDescriptor
            1    -> chilrenDescriptor
            else -> error("bad index: $index")
        }
    }

    override fun getElementIndex(name: String): Int {
        return when (name) {
            "value"    -> 0
            "children" -> 1
            else       -> error("bad name: $name")
        }
    }

    override fun getElementName(index: Int): String {
        return when (index) {
            0    -> "value"
            1    -> "children"
            else -> error("bad index: $index")
        }
    }

    override fun isElementOptional(index: Int): Boolean {
        return when (index) {
            0    -> false
            1    -> true
            else -> error("bad index: $index")
        }
    }

}

@OptIn(ExperimentalSerializationApi::class)
class TreeNodeSerializer<T>(private val elementSerializer: KSerializer<T>) : KSerializer<TreeDataNode<T>> {
    override val descriptor by lazy {
        TreeNodeSerialDescriptor(elementSerializer.descriptor)
    }

    override fun deserialize(decoder: Decoder): TreeDataNode<T> {

        return decoder.decodeStructure(descriptor) {
            var theValue: Value<T>? = null
            var children: Value<List<TreeDataNode<T>>>? = null
            while (true) {
                val index = decodeElementIndex(descriptor)
                when (index) {
                    0            -> {
                        val e = decodeSerializableElement(descriptor, index, elementSerializer)
                        theValue = Value(e)
                    }

                    1            -> {
                        val c = decodeSerializableElement(descriptor, index, ListSerializer(this@TreeNodeSerializer))
                        children = Value(c)
                    }

                    DECODE_DONE  -> break
                    UNKNOWN_NAME -> error("unknown name?")

                    else         -> error("Unexpected index: $index")

                }
            }

            requireNotNull(theValue)

            TreeDataNode(value = theValue.value, children = children?.value)

        }


    }

    override fun serialize(
        encoder: Encoder,
        value: TreeDataNode<T>
    ) {

        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, elementSerializer, value.value)
            value.children?.go { children ->
                encodeSerializableElement(descriptor, 1, ListSerializer(this@TreeNodeSerializer), children)
            }
        }

    }

}

interface TreeDataNodeInter<T> {
    val value: T
    val children: List<TreeDataNodeInter<T>>?
}

typealias MTreeNode<T> = TreeDataNodeInter<T>

abstract class TreeDataNodeBase<T, N : TreeDataNodeBase<T, N>> : TreeDataNodeInter<T> {
    override fun equals(other: Any?): Boolean {
        return other is TreeDataNodeInter<*>
                && other.value == value
                && other.children == children
    }

    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + (children?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "${this::class.simpleName}[value=$value,size=${children?.size}]"
    }
}

@Serializable(with = TreeNodeSerializer::class)
class TreeDataNode<T>(
    override val value: T,
    override val children: List<TreeDataNode<T>>? = null
) :
    TreeDataNodeBase<T, TreeDataNode<T>>()

class MutableTreeDataNode<T>(
    override val value: T,
    override val children: MutableList<MutableTreeDataNode<T>> = mutableListOf()
) :
    TreeDataNodeBase<T, MutableTreeDataNode<T>>()


fun <T, R> MTreeNode<T>.map(op: (T) -> R): TreeDataNode<R> {
    val newValue: R = op(value)
    val newChildren: List<TreeDataNode<R>>? = children?.map { it.map(op) }
    return TreeDataNode(newValue, newChildren)
}

fun <T> MTreeNode<T>.allValuesRecursive(): List<T> {
    val r = mutableListOf<T>(value)
    children?.forEach {
        r.addAll(it.allValuesRecursive())
    }
    return r.readOnly()
}
