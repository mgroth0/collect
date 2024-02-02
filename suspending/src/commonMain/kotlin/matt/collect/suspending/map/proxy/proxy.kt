package matt.collect.suspending.map.proxy

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import matt.collect.mapToSet
import matt.collect.suspending.SuspendMutableCollection
import matt.collect.suspending.ext.map
import matt.collect.suspending.ext.toSet
import matt.collect.suspending.list.fake.toSuspendingFakeMutableList
import matt.collect.suspending.list.suspending
import matt.collect.suspending.map.SuspendEntry
import matt.collect.suspending.map.SuspendMutableEntry
import matt.collect.suspending.map.SuspendMutableMap
import matt.collect.suspending.map.toFakeSuspendMutableEntry
import matt.collect.suspending.set.SuspendMutableSet
import matt.collect.suspending.set.fake.toSuspendingFakeMutableSet
import matt.lang.convert.BiConverter
import matt.model.data.proxy.map.ImmutableProxyMap
import kotlin.collections.Map.Entry


fun <SK : Any, SV : Any, TK : Any, TV : Any> SuspendMutableMap<SK, SV>.proxy(
    keyConverter: BiConverter<SK, TK>,
    valueConverter: BiConverter<SV, TV>
) = SuspendProxyMap(
    this,
    keyConverter,
    valueConverter
)

class SuspendProxyMap<SK : Any, SV : Any, TK : Any, TV : Any>(
    private val innerMap: SuspendMutableMap<SK, SV>,
    private val keyConverter: BiConverter<SK, TK>,
    private val valueConverter: BiConverter<SV, TV>
) : SuspendMutableMap<TK, TV> {

    private fun SK.toTK() = keyConverter.convertToB(this)
    private fun SV.toTV() = valueConverter.convertToB(this)
    private fun TK.toSK() = keyConverter.convertToA(this)
    private fun TV.toSV() = valueConverter.convertToA(this)

    override suspend fun currentEntries(): SuspendMutableSet<out SuspendMutableEntry<TK, TV?>> = innerMap.currentEntries().map { e ->
        object : SuspendEntry<TK, TV?> {
            override suspend fun key() = e.key().toTK()
            override suspend fun value() = e.value()?.toTV()
        }.toFakeSuspendMutableEntry()
    }.toSet().toSuspendingFakeMutableSet()

    override fun entry(key: TK): SuspendMutableEntry<TK, TV?> {
        val skKey = key.toSK()
        val e = innerMap.entry(skKey)
        return object : SuspendMutableEntry<TK, TV?> {
            override suspend fun setValue(newValue: TV?): TV? = e.setValue(newValue?.toSV())?.toTV()

            override suspend fun key(): TK = key

            override suspend fun value(): TV? = e.value()?.toTV()

        }
    }


    override suspend fun keys(): SuspendMutableSet<TK> =
        currentEntries().map { it.key() }.toSet().toSuspendingFakeMutableSet()

    override suspend fun size(): Int = innerMap.size()
    override suspend fun values(): SuspendMutableCollection<TV> =

        snapshot().map { it.value }.suspending().toSuspendingFakeMutableList()

    override suspend fun clear() = innerMap.clear()
    override suspend fun snapshot(): Set<Entry<TK, TV>> {
        return innerMap.snapshot().mapToSet { e ->
            object : Map.Entry<TK, TV> {
                override val key = e.key.toTK()
                override val value = e.value.toTV()

            }
        }
//        toNonSuspendMap().entries
    }

    override suspend fun isEmpty() = innerMap.isEmpty()
    override suspend fun toNonSuspendMap(): Map<TK, TV> = ImmutableProxyMap(
        innerMap.toNonSuspendMap(),
        keyConverter,
        valueConverter
    )

    override suspend fun remove(key: TK) = innerMap.remove(key.toSK())?.toTV()

    override suspend fun putAll(from: Map<out TK, TV>) {
        innerMap.putAll(from.mapKeys { it.key.toSK() }.mapValues { it.value.toSV() })
    }

    override suspend fun put(
        key: TK,
        value: TV
    ): TV? = innerMap.put(
        key.toSK(),
        value.toSV()
    )?.toTV()

    override suspend fun get(key: TK): TV? = innerMap.get(key.toSK())?.toTV()

    override suspend fun containsValue(value: TV): Boolean = innerMap.containsValue(value.toSV())

    override suspend fun containsKey(key: TK): Boolean = innerMap.containsKey(key.toSK())

    override suspend fun chunkFlow(): Flow<Map<TK, TV>> = innerMap.chunkFlow().map { it.mapKeys { it.key.toTK() }.mapValues { it.value.toTV() } }

}
