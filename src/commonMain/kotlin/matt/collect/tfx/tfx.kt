@file:OptIn(ExperimentalStdlibApi::class)

package matt.collect.tfx



/**
 * [forEach] with Map.Entree as receiver.
 */
inline fun <K, V> Map<K, V>.withEach(action: Map.Entry<K, V>.()->Unit) = forEach(action)

/**
 * [forEach] with the element as receiver.
 */
inline fun <T> Iterable<T>.withEach(action: T.()->Unit) = forEach(action)

/**
 * [forEach] with the element as receiver.
 */
inline fun <T> Sequence<T>.withEach(action: T.()->Unit) = forEach(action)

/**
 * [forEach] with the element as receiver.
 */
inline fun <T> Array<T>.withEach(action: T.()->Unit) = forEach(action)

/**
 * [map] with Map.Entree as receiver.
 */
inline fun <K, V, R> Map<K, V>.mapEach(action: Map.Entry<K, V>.()->R) = map(action)

/**
 * [map] with the element as receiver.
 */
inline fun <T, R> Iterable<T>.mapEach(action: T.()->R) = map(action)

/**
 * [map] with the element as receiver.
 */
fun <T, R> Sequence<T>.mapEach(action: T.()->R) = map(action)

/**
 * [map] with the element as receiver.
 */
inline fun <T, R> Array<T>.mapEach(action: T.()->R) = map(action)

/**
 * [mapTo] with Map.Entree as receiver.
 */
inline fun <K, V, R, C: MutableCollection<in R>> Map<K, V>.mapEachTo(destination: C, action: Map.Entry<K, V>.()->R) =
    mapTo(destination, action)

/**
 * [mapTo] with the element as receiver.
 */
inline fun <T, R, C: MutableCollection<in R>> Iterable<T>.mapEachTo(destination: C, action: T.()->R) =
    mapTo(destination, action)

/**
 * [mapTo] with the element as receiver.
 */
fun <T, R, C: MutableCollection<in R>> Sequence<T>.mapEachTo(destination: C, action: T.()->R) =
    mapTo(destination, action)

/**
 * [mapTo] with the element as receiver.
 */
fun <T, R, C: MutableCollection<in R>> Array<T>.mapEachTo(destination: C, action: T.()->R) = mapTo(destination, action)




/**
 * Moves the given **T** item to the specified index
 */
fun <T> MutableList<T>.move(item: T, newIndex: Int) {
    check(newIndex in 0 ..< size)
    val currentIndex = indexOf(item)
    if (currentIndex < 0) return
    removeAt(currentIndex)
    add(newIndex, item)
}




/**
 * Moves the given item at the `oldIndex` to the `newIndex`
 */
fun <T> MutableList<T>.moveAt(oldIndex: Int, newIndex: Int) {
    check(oldIndex in 0 ..< size)
    check(newIndex in 0 ..< size)
    val item = this[oldIndex]
    removeAt(oldIndex)
    add(newIndex, item)
}

/**
 * Moves all items meeting a predicate to the given index
 */
fun <T> MutableList<T>.moveAll(newIndex: Int, predicate: (T)->Boolean) {
    check(newIndex in 0 ..< size)
    val split = partition(predicate)
    clear()
    addAll(split.second)
    addAll(if (newIndex >= size) size else newIndex, split.first)
}

/**
 * Moves the given element at specified index up the **MutableList** by one increment
 * unless it is at the top already which will result in no movement
 */
fun <T> MutableList<T>.moveUpAt(index: Int) {
    if (index == 0) return
    check(index in indices, { "Invalid index $index for MutableList of size $size" })
    val newIndex = index - 1
    val item = this[index]
    removeAt(index)
    add(newIndex, item)
}

/**
 * Moves the given element **T** up the **MutableList** by one increment
 * unless it is at the bottom already which will result in no movement
 */
fun <T> MutableList<T>.moveDownAt(index: Int) {
    if (index == size - 1) return
    check(index in indices, { "Invalid index $index for MutableList of size $size" })
    val newIndex = index + 1
    val item = this[index]
    removeAt(index)
    add(newIndex, item)
}

/**
 * Moves the given element **T** up the **MutableList** by an index increment
 * unless it is at the top already which will result in no movement.
 * Returns a `Boolean` indicating if move was successful
 */
fun <T> MutableList<T>.moveUp(item: T): Boolean {
    val currentIndex = indexOf(item)
    if (currentIndex == -1) return false
    val newIndex = (currentIndex - 1)
    if (currentIndex <= 0) return false
    remove(item)
    add(newIndex, item)
    return true
}

/**
 * Moves the given element **T** up the **MutableList** by an index increment
 * unless it is at the bottom already which will result in no movement.
 * Returns a `Boolean` indicating if move was successful
 */
fun <T> MutableList<T>.moveDown(item: T): Boolean {
    val currentIndex = indexOf(item)
    if (currentIndex == -1) return false
    val newIndex = (currentIndex + 1)
    if (newIndex >= size) return false
    remove(item)
    add(newIndex, item)
    return true
}


/**
 * Moves first element **T** up an index that satisfies the given **predicate**, unless its already at the top
 */
inline fun <T> MutableList<T>.moveUp(crossinline predicate: (T)->Boolean) = find(predicate)?.let { moveUp(it) }

/**
 * Moves first element **T** down an index that satisfies the given **predicate**, unless its already at the bottom
 */
inline fun <T> MutableList<T>.moveDown(crossinline predicate: (T)->Boolean) = find(predicate)?.let { moveDown(it) }

/**
 * Moves all **T** elements up an index that satisfy the given **predicate**, unless they are already at the top
 */
inline fun <T> MutableList<T>.moveUpAll(crossinline predicate: (T)->Boolean) = asSequence().withIndex()
    .filter { predicate.invoke(it.value) }
    .forEach { moveUpAt(it.index) }

/**
 * Moves all **T** elements down an index that satisfy the given **predicate**, unless they are already at the bottom
 */
inline fun <T> MutableList<T>.moveDownAll(crossinline predicate: (T)->Boolean) = asSequence().withIndex()
    .filter { predicate.invoke(it.value) }
    .forEach { moveDownAt(it.index) }


fun <T> MutableList<T>.moveToTopWhere(predicate: (T)->Boolean) {
    asSequence().filter(predicate).toList().asSequence().forEach {
        remove(it)
        add(0, it)
    }
}

fun <T> MutableList<T>.moveToBottomWhere(predicate: (T)->Boolean) {
    val end = size - 1
    asSequence().filter(predicate).toList().asSequence().forEach {
        remove(it)
        add(end, it)
    }
}

