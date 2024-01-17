package matt.collect.suspending

import matt.collect.suspending.ext.suspendSetOf
import matt.collect.suspending.list.SuspendWrapList
import matt.collect.suspending.list.SuspendWrapListIterator
import matt.collect.suspending.list.fake.toSuspendingFakeMutableList
import matt.collect.suspending.list.proxy.SuspendProxyList
import matt.collect.suspending.list.suspending
import matt.collect.suspending.set.fake.FakeMutableSuspendSet
import matt.model.op.convert.LongMillisConverter
import matt.test.co.runTestWithTimeoutOnlyIfTestingPerformance
import matt.test.scaffold.TestScaffold


class SuspendingCollectionsTest : TestScaffold() {
    override fun initEnums() {
    }

    override fun initObjects() {

        listOf(1).suspending()
        SuspendProxyList(
            SuspendWrapList(listOf(1L)).toSuspendingFakeMutableList(),
            LongMillisConverter
        )
        SuspendWrapListIterator(listOf(1).listIterator())
    }

    override fun initVals() {
    }

    override fun instantiateClasses() {
        runTestWithTimeoutOnlyIfTestingPerformance {
            FakeMutableSuspendSet(suspendSetOf<Int>())
        }

    }

    override fun runFunctions() {
    }

}