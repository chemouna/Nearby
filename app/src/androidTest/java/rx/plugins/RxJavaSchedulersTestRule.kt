package rx.plugins

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.schedulers.Schedulers

/**
 * A {@link TestRule} to apply to tests that have observables that subscribe/observe on
 * RxJava/RxAndroid schedulers.
 *
 * NOTE: we needed to put in package rx.plugins to be able to access reset method that has
 * a package private (there's currently an issue on RxJava to make it public).
 */
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
