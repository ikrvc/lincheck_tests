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

@Param(name = "key", gen = IntGen::class, conf = "0:3")
class CaffeineInvalidateAllTests {

    private val cache: Cache<Int, String> = Caffeine.newBuilder()
        .maximumSize(10)
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
    fun invalidateAll() {
        cache.invalidateAll()
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