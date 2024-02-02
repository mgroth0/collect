package matt.collect.test

import matt.collect.fake.FakeMutableList
import matt.collect.itr.FakeMutableIterator
import matt.collect.itr.FakeMutableListIterator
import matt.collect.itr.ItrDir
import matt.collect.itr.LoopIterator
import matt.collect.itr.LoopListIterator
import matt.collect.itr.MutableIteratorWithSomeMemory
import matt.collect.itr.MutableIteratorWrapper
import matt.collect.itr.MutableListIteratorWithSomeMemory
import matt.collect.itr.MutableListIteratorWrapper
import matt.collect.itr.MutableLoopIterator
import matt.collect.itr.MutableLoopListIterator
import matt.collect.itr.loop.Loop
import matt.collect.itr.recurse.chain.chain
import matt.collect.itr.recurse.depth.recursionDepth
import matt.collect.itr.recurse.recurse
import matt.collect.lazy.basic.MutableLazyList
import matt.collect.lazy.seq.LazySequenceList
import matt.collect.lazy.set.LazySet
import matt.collect.list.single.SingleElementListImpl
import matt.collect.map.dmap.withStoringDefault
import matt.collect.weak.bag.WeakBag
import matt.lang.model.value.LazyValue
import matt.test.Tests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CollectTests : Tests() {
    @Test
    fun constructClasses() =
        assertRunsInOneMinute {
            SingleElementListImpl(1)
            LazySet(sequenceOf(1, 2, 3).iterator())
            WeakBag<Int>()
            MutableLazyList(listOf(LazyValue { 1 }))
            LazySequenceList(sequenceOf(1, 2, 3))
            FakeMutableList(listOf(1, 2, 3))
            Loop(listOf(1, 2, 3))
            FakeMutableListIterator(listOf(1, 2, 3).listIterator())
            FakeMutableIterator(setOf(1, 2, 3).iterator())
            LoopIterator(listOf(1, 2, 3))
            MutableLoopIterator(mutableListOf(1, 2, 3))
            LoopListIterator(listOf(1, 2, 3))
            MutableLoopListIterator(mutableListOf(1, 2, 3))
            MutableIteratorWrapper(mutableListOf(1))
            MutableListIteratorWrapper(mutableListOf(1))
            MutableIteratorWithSomeMemory(mutableListOf(1))
            MutableListIteratorWithSomeMemory(mutableListOf(1))
        }

    @Test
    fun runFunctions() {
        1.chain {
            null
        }
        1.recursionDepth {
            listOf()
        }
        1.recurse {
            listOf()
        }
    }

    @Test
    fun iniEnums() {
        ItrDir.entries
    }

    @Test
    fun testMapImplementation() {
        val m = mutableMapOf<Int, String>().withStoringDefault { it.toString() }
        assertTrue(m.isEmpty())
        m[1]
        assertEquals(1, m.size)
        assertEquals("1", m.getWithoutSetting(1))
    }
}
