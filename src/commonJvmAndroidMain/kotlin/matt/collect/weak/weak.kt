
package matt.collect.weak

import java.util.Collections
import java.util.WeakHashMap

actual class WeakMap<K, V> : MutableMap<K, V> by Collections.synchronizedMap(
    WeakHashMap()
) /*need to do this or threads could deadlock or have any other weird issue (deadlocking HAS happened though) since WeakMap is not thread-safe. This issue has been discussed online, and this is the proper solution (also see WeakHashMap, where they say to do this) */

