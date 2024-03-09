
package matt.collect.props

import java.io.InputStream
import java.util.Properties

fun Properties(inputStream: InputStream): Properties {
    val props = Properties()
    props.load(inputStream)
    return props
}

fun propertiesOf(vararg entries: Pair<String, String>): Properties {
    val props = Properties()
    props.putAll(entries)
    return props
}



