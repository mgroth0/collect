package matt.collect.tree.impl.dag.ser

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import matt.collect.tree.impl.dag.Dag
import matt.collect.tree.impl.dag.DirectedAcyclicGraph.Serialized
import matt.lang.anno.Coordinated

@Coordinated(63433525)
class DagSerializer<T>(private val vertexSerializer: KSerializer<T>): KSerializer<Dag<T>> {


    override val descriptor =
        buildClassSerialDescriptor(
            "DirectedAcyclicGraph",
            vertexSerializer.descriptor
        ) {
            element(Serialized<*>::vertices.name, MapSerializer(serializer<Int>(), vertexSerializer).descriptor)
            element(Serialized<*>::edges.name, SetSerializer(ListSerializer(serializer<Int>())).descriptor)
        }

    @OptIn(ExperimentalSerializationApi::class)
    private val ser =
        serializer(
            Serialized::class,
            listOf(vertexSerializer),
            isNullable = false
        )

    @Coordinated(63433525)
    override fun deserialize(decoder: Decoder): Dag<T> {

        val compositeDecoder =  decoder.beginStructure(descriptor)


        var verts:  Map<Int, T>? = null
        var edges:  Set<List<Int>>? = null
        while (true) {
            when (val index = compositeDecoder.decodeElementIndex(descriptor)) {
                0 ->
                    verts =
                        compositeDecoder.decodeSerializableElement(
                            descriptor,
                            0,
                            MapSerializer(serializer<Int>(), vertexSerializer)
                        )
                1 ->
                    edges =
                        compositeDecoder.decodeSerializableElement(
                            descriptor,
                            1,
                            SetSerializer(ListSerializer(serializer<Int>()))
                        )
                DECODE_DONE -> break /* Input is over */
                else -> error("Unexpected index: $index")
            }
        }





        compositeDecoder.endStructure(descriptor)

        /*val partiallyDecoded = decoder.decodeSerializableValue(serializer<Serialized<>>)




        val decoded = decoder.decodeSerializableValue(ser)*/
        val awesomeDecoded = Serialized(verts!!, edges!!)
        /*val casted = decoded as Serialized<*>*/
        val decompressed = awesomeDecoded.decompress()
        return decompressed
    }

    override fun serialize(
        encoder: Encoder,
        value: Dag<T>
    ) {
        val compressed = value.compress()
        encoder.encodeSerializableValue(ser, compressed)
    }
}
