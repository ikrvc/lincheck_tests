package src.caffeine

import com.github.benmanes.caffeine.cache.*
import org.jetbrains.kotlinx.lincheck.annotations.*
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.paramgen.IntGen
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.junit.Test
import java.util.concurrent.TimeUnit

@Param(name = "key", gen = IntGen::class, conf = "0:2")
class CaffeineStatsTests {

    private val cache: Cache<Int, String> = Caffeine.newBuilder()
        .maximumSize(10)
        .recordStats()                     // Enable stats tracking
        .executor(Runnable::run)
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
    fun getHitCount(): Long {
        return cache.stats().hitCount()
    }

    @Operation
    fun getMissCount(): Long {
        return cache.stats().missCount()
    }

    @Operation
    fun getRequestCount(): Long {
        return cache.stats().requestCount()
    }

    @Test
    fun modelTest() {
        ModelCheckingOptions()
            .iterations(300)
            .invocationsPerIteration(30)
            .threads(2)
            .actorsPerThread(2)
            .check(this::class)
    }
}
