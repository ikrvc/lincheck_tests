package src.caffeine

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.jetbrains.kotlinx.lincheck.annotations.*
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.paramgen.IntGen
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Test
import org.junit.BeforeClass


/**
 * Tests the behavior of Caffeine's `cleanUp()` method under concurrent use.
 * Focuses on interactions between manual eviction and get/put operations.
 */
@Param(name = "key", gen = IntGen::class, conf = "0:3")
class CaffeineCleanupTests {

    private val cache: Cache<Int, String> = Caffeine.newBuilder()
    .maximumSize(5)
    .executor(Runnable::run) // disables asynchronous behavior
    .build()

    @Operation
    fun put(@Param(name = "key") key: Int, value: String = "v$key") {
        cache.put(key, value)
    }

    @Operation
    fun getIfPresent(@Param(name = "key") key: Int): String? {
        return cache.getIfPresent(key)
    }

    @Operation
    fun cleanUp() {
        cache.cleanUp()
    }

//    @Test
//    fun stressTest() {
//        StressOptions().check(this::class)
//    }

//    @Test
//    fun modelTest() = runBlocking {
//        try {
//            withTimeout(60_000) { // 60 seconds
//                ModelCheckingOptions()
//                    .iterations(500)
//                    .invocationsPerIteration(50)
//                    .threads(2)
//                    .actorsPerThread(2)
//                    .check(this::class)
//            }
//        } catch (e: Exception) {
//            println("Timeout or failure: ${e.message}")
//        }
//    }

    @Test
    fun modelTest() {
        ModelCheckingOptions()
            .iterations(500)
            .invocationsPerIteration(100)    // each thread does fewer ops
            .threads(2)
            .actorsPerThread(1)             // increases concurrency
            .check(this::class)
    }

//    @Test
//    fun modelTest() {
//        ModelCheckingOptions().check(this::class)
//    }
}
