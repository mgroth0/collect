package matt.collect.tree.impl.dag.ser

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import matt.collect.tree.impl.dag.Dag
import matt.collect.tree.impl.dag.DirectedAcyclicGraph.Serialized
import matt.lang.anno.Coordinated

@Coordinated(63433525)
class DagSerializer<T>(vertexSerializer: KSerializer<T>): KSerializer<Dag<T>> {


    override val descriptor =
        buildClassSerialDescriptor(
            "DirectedAcyclicGraph",
            vertexSerializer.descriptor
        ) {
            element(Serialized<*>::vertices.name, MapSerializer(serializer<Int>(), vertexSerializer).descriptor)
            element(Serialized<*>::edges.name, SetSerializer(ListSerializer(serializer<Int>())).descriptor)
        }

    private val ser =
        serializer(
            Serialized::class,
            listOf(vertexSerializer),
            isNullable = false
        )

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): Dag<T> = (decoder.decodeSerializableValue(ser) as Serialized<T>).decompress()

    override fun serialize(
        encoder: Encoder,
        value: Dag<T>
    ) {
        val compressed = value.compress()
        encoder.encodeSerializableValue(ser, compressed)
    }
}
