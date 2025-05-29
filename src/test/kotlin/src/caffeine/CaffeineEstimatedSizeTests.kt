package src.caffeine

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.annotations.Param
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.paramgen.IntGen
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.Test

/**
 * Testing the cache size with limited size.
 *
 * Estimated size function is not always accurate.
 */
@Param(name = "key", gen = IntGen::class, conf = "0:5")
class CaffeineEstimatedSizeTests {

    private val cache: Cache<Int, String> = Caffeine.newBuilder()
        .maximumSize(3)
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
    fun estimatedSize(): Long {
        return cache.estimatedSize()
    }

    @Test
    fun stressTest() {
        StressOptions().check(this::class)
    }

    @Test
    fun modelTest() {
        ModelCheckingOptions().check(this::class)
    }
}