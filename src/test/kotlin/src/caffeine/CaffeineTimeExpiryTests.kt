package src.caffeine

import ManualTicker
import com.github.benmanes.caffeine.cache.*
import org.jetbrains.kotlinx.lincheck.annotations.*
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.paramgen.IntGen
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.junit.Test
import java.util.concurrent.TimeUnit

@Param(name = "key", gen = IntGen::class, conf = "0:3")
class CaffeineTimeExpiryTests {

    // Fake clock (manual time control)
    private val ticker = ManualTicker()

    private val cache: Cache<Int, String> = Caffeine.newBuilder()
        .maximumSize(5)
        .expireAfterWrite(5, TimeUnit.SECONDS)
//        .expireAfterAccess(3, TimeUnit.SECONDS)
        .ticker(ticker)
        .executor(Runnable::run) // synchronous execution
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
    fun advanceTime(@Param(name = "key") seconds: Int) {
        // We abuse the IntGen for seconds: conf = 0:2 -> 0s, 1s, or 2s
        ticker.advance(seconds.toLong(), TimeUnit.SECONDS)
    }

    @Operation
    fun estimatedSize(): Long {
        return cache.estimatedSize()
    }

    @Test
    fun modelTest() {
        ModelCheckingOptions()
            .iterations(500)
            .invocationsPerIteration(50)
            .threads(2)
            .actorsPerThread(2)
            .check(this::class)
    }
}