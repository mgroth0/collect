package matt.collect.test


import matt.collect.lazy.basic.MutableLazyList
import matt.collect.lazy.seq.LazySequenceList
import matt.collect.lazy.set.LazySet
import matt.collect.list.single.SingleElementList
import matt.collect.weak.bag.WeakBag
import matt.lang.model.value.LazyValue
import matt.test.JupiterTestAssertions.assertRunsInOneMinute
import kotlin.test.Test

class CollectTests {
    @Test
    fun constructClasses() = assertRunsInOneMinute {
        SingleElementList(1)
        LazySet(sequenceOf(1, 2, 3).iterator())
        WeakBag<Int>()
        MutableLazyList(listOf(LazyValue { 1 }))
        LazySequenceList(sequenceOf(1, 2, 3))
    }
}