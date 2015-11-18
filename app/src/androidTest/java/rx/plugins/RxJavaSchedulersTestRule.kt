package rx.plugins

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.schedulers.Schedulers

public class RxJavaSchedulersTestRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                resetPlugins()
                RxJavaPlugins.getInstance().registerSchedulersHook(TestRxJavaSchedulersHook())
                RxAndroidPlugins.getInstance().registerSchedulersHook(TestRxAndroidSchedulersHook())

                base.evaluate()

                resetPlugins()
            }
        }
    }

    private fun resetPlugins() {
        RxJavaPlugins.getInstance().reset()
        RxAndroidPlugins.getInstance().reset()
    }

    private inner class TestRxAndroidSchedulersHook : RxAndroidSchedulersHook() {
        override fun getMainThreadScheduler(): Scheduler {
            return Schedulers.immediate()
        }
    }

    private inner class TestRxJavaSchedulersHook : RxJavaSchedulersHook() {
        override fun getIOScheduler(): Scheduler {
            return Schedulers.immediate()
        }
    }
}
