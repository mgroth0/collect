package matt.collect.tree.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind.CLASS
import kotlinx.serialization.descriptors.setSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import matt.collect.tree.impl.linear.LinearTreeNode
import matt.collect.tree.impl.linear.data.LinearTreeDataNode
import matt.collect.tree.inter.TreeValue
import matt.lang.model.value.Value


/* See [[ListLikeDescriptor]] */
@ExperimentalSerializationApi
class TreeNodeSerialDescriptor(private val elementDescriptor: SerialDescriptor) : SerialDescriptor {
    override val elementsCount: Int = 2

    override val kind: SerialKind = CLASS

    override val serialName: String = "TreeDataNode"

    private fun badIndexError(index: Int): Nothing = error("bad index: $index")

    override fun getElementAnnotations(index: Int): List<Annotation> =
        when (index) {
            0 -> listOf()
            1 -> listOf()
            else -> badIndexError(index)
        }

    val chilrenDescriptor by lazy {
        setSerialDescriptor(TreeNodeSerialDescriptor(elementDescriptor))
    }

    override fun getElementDescriptor(index: Int): SerialDescriptor =
        when (index) {
            0 -> elementDescriptor
            1 -> chilrenDescriptor
            else -> badIndexError(index)
        }

    override fun getElementIndex(name: String): Int =
        when (name) {
            "value" -> 0
            "children" -> 1
            else -> error("bad name: $name")
        }

    override fun getElementName(index: Int): String =
        when (index) {
            0 -> "value"
            1 -> "children"
            else -> badIndexError(index)
        }

    override fun isElementOptional(index: Int): Boolean =
        when (index) {
            0 -> false
            1 -> true
            else -> badIndexError(index)
        }
}


class TreeDataNodeSerializer<T>(elementSerializer: KSerializer<T>) :
    TreeValueSerializer<T, LinearTreeDataNode<T>>(elementSerializer) {
    override fun construct(
        value: T,
        children: Set<LinearTreeDataNode<T>>
    ): LinearTreeDataNode<T> = LinearTreeDataNode(value, children)
}

class TreeNodeSerializer<T>(elementSerializer: KSerializer<T>) :
    TreeValueSerializer<T, LinearTreeNode<T>>(elementSerializer) {
    override fun construct(
        value: T,
        children: Set<LinearTreeNode<T>>
    ): LinearTreeNode<T> = LinearTreeNode(value, children)
}

@OptIn(ExperimentalSerializationApi::class)
abstract class TreeValueSerializer<T, N : TreeValue<T, N>>(private val elementSerializer: KSerializer<T>) :
    KSerializer<N> {
    final override val descriptor by lazy {
        TreeNodeSerialDescriptor(elementSerializer.descriptor)
    }

    final override fun deserialize(decoder: Decoder): N =
        decoder.decodeStructure(descriptor) {
            var theValue: Value<T>? = null
            var children: Value<Set<N>>? = null
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0                             -> {
                        val e = decodeSerializableElement(descriptor, index, elementSerializer)
                        theValue = Value(e)
                    }

                    1 -> {
                        val c =
                            decodeSerializableElement(
                                descriptor,
                                index,
                                SetSerializer(this@TreeValueSerializer)
                            )
                        children = Value(c)
                    }

                    CompositeDecoder.DECODE_DONE  -> break
                    CompositeDecoder.UNKNOWN_NAME -> error("unknown name?")

                    else -> error("Unexpected index: $index")
                }
            }

            requireNotNull(theValue)

            construct(value = theValue.value, children = children?.value ?: emptySet<N>())
        }

    abstract fun construct(
        value: T,
        children: Set<N>
    ): N

    final override fun serialize(
        encoder: Encoder,
        value: N
    ) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, elementSerializer, value.value)
            val children = value.children
            encodeSerializableElement(
                descriptor,
                1,
                SetSerializer(this@TreeValueSerializer),
                children
            )
        }
    }
}

