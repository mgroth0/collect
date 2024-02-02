package matt.collect.sprops

import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.decodeFromMap
import kotlinx.serialization.properties.encodeToMap


inline fun <reified T: Any> java.util.Properties.toKotlinProperties()
    = Properties.decodeFromMap<T>(
        mapKeys { it.key as String }
    )


inline fun <reified T: Any> T.serializeToProperties()
    =  java.util.Properties().also { p ->
        Properties.encodeToMap<T>(this).forEach { (k, v) -> p[k] = v }
    }
