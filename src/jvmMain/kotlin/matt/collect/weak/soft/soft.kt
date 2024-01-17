package matt.collect.weak.soft

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder

@Deprecated("Supposed to use Caffeine instead. See the Javadoc for CacheBuilder.")
fun <K : Any, V : Any> softMap(op: (K) -> V): Cache<K, V> = CacheBuilder.newBuilder().softValues().build()