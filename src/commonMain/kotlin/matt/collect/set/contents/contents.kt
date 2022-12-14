package matt.collect.set.contents

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
class ContentsSerializer<E>(private val dataSerializer: KSerializer<E>): KSerializer<Contents<E>> {
  override val descriptor: SerialDescriptor = listSerialDescriptor(dataSerializer.descriptor)

  private val listSer by lazy {
	ListSerializer(dataSerializer)
  }

  override fun serialize(encoder: Encoder, value: Contents<E>) {
	encoder.encodeSerializableValue(listSer, value.toList())

  }

  override fun deserialize(decoder: Decoder): Contents<E> {
	return Contents(decoder.decodeSerializableValue(listSer))
  }


}


fun <E> contentsOf(vararg e: E) = Contents(*e)
fun <E> Iterable<E>.toContents() = Contents(this)
fun <E> Sequence<E>.toContents() = Contents(this)


@Serializable(with = ContentsSerializer::class)
class Contents<E>(set: Set<E>): Set<E> by set {

  constructor(itr: Iterable<E>): this(itr.toSet())
  constructor(itr: Sequence<E>): this(itr.toSet())
  constructor(vararg e: E): this(e.toSet())

  override fun equals(other: Any?): Boolean {
	return other is Contents<*> && other.size == size && containsAll(other)
  }

  override fun hashCode(): Int {
	return map { it.hashCode() }.sum()
  }
}
