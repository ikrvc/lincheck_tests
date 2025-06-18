package src.caffeine

import com.github.benmanes.caffeine.cache.*
import org.jetbrains.kotlinx.lincheck.annotations.*
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.paramgen.IntGen
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.junit.Test
import java.util.concurrent.TimeUnit

@Param(name = "key", gen = IntGen::class, conf = "0:2")
@Param(name = "seconds", gen = IntGen::class, conf = "1:5")
class CaffeineAsMapExpiryTests {

//    private val ticker = ManualTicker()

    private val cache: Cache<Int, String> = Caffeine.newBuilder()
        .maximumSize(5)
        .expireAfterWrite(5, TimeUnit.SECONDS)
//        .ticker(ticker)
        .executor(Runnable::run)
        .build()

    @Operation
    fun put(@Param(name = "key") key: Int, value: String = "v$key") {
        cache.put(key, value)
    }

    @Operation
    fun getFromMap(@Param(name = "key") key: Int): String? {
        return cache.asMap()[key]
    }

    @Operation
    fun containsKey(@Param(name = "key") key: Int): Boolean {
        return cache.asMap().containsKey(key)
    }

    @Operation
    fun invalidate(@Param(name = "key") key: Int) {
        cache.invalidate(key)
    }

    @Operation
    fun cleanUp() {
        cache.cleanUp()
    }

//    @Operation
//    fun advanceTime(@Param(name = "seconds") seconds: Int) {
//        ticker.advance(seconds.toLong(), TimeUnit.SECONDS)
//    }

    @Operation
    fun getThenContains(@Param(name = "key") key: Int): Boolean {
        val v = cache.asMap()[key]
        return if (v != null) cache.asMap().containsKey(key) else true //If the value was null, we don't care what containsKey() returns then we just return true to avoid false positives.
    }

    @Test
    fun modelTest() {
        ModelCheckingOptions()
            .iterations(200)
            .invocationsPerIteration(8)
            .threads(2)
            .actorsPerThread(2)
            .check(this::class)
    }

//  @Test
//  fun modelTest() {
//      ModelCheckingOptions().check(this::class)
//  }

}
