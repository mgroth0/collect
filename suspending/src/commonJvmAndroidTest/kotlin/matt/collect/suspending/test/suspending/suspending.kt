package matt.collect.suspending.test.suspending

import matt.collect.suspending.ext.suspendSetOf
import matt.collect.suspending.list.SuspendWrapList
import matt.collect.suspending.list.SuspendWrapListIterator
import matt.collect.suspending.list.fake.toSuspendingFakeMutableList
import matt.collect.suspending.list.proxy.SuspendProxyList
import matt.collect.suspending.list.suspending
import matt.collect.suspending.set.fake.FakeMutableSuspendSet
import matt.model.op.convert.LongMillisConverter
import matt.test.co.runTestWithTimeoutOnlyIfTestingPerformance
import matt.test.jcommon.Tests
import kotlin.test.Test


class SuspendingCollectionsTest :Tests() {
    @Test fun initEnums() {
    }

    @Test fun initObjects() {

        listOf(1).suspending()
        SuspendProxyList(
            SuspendWrapList(listOf(1L)).toSuspendingFakeMutableList(),
            LongMillisConverter
        )
        SuspendWrapListIterator(listOf(1).listIterator())
    }

    @Test fun initVals() {
    }

    @Test fun instantiateClasses() {
        runTestWithTimeoutOnlyIfTestingPerformance {
            FakeMutableSuspendSet(suspendSetOf<Int>())
        }
    }

    @Test fun runFunctions() {
    }
}
