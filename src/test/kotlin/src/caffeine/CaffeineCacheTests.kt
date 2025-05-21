package src.caffeine

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.Cache

import org.jetbrains.kotlinx.lincheck.annotations.*
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.paramgen.IntGen
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.Test

@Param(name = "key", gen = IntGen::class, conf = "0:3")
class CaffeineCacheTests {

    private val cache: Cache<Int, String> = Caffeine.newBuilder()
        .maximumSize(10)
        .executor(Runnable::run)
        .build()

    @Operation
    fun getIfPresent(@Param(name = "key") key: Int): String? {
        return cache.getIfPresent(key)
    }

    @Operation
    fun get(@Param(name = "key") key: Int): String? {
        return cache.get(key) { k -> "computed-$k" }
    }

    //Need to be finished
//    @Operation
//    fun getAllPresent() {
//        cache.getAllPresent()
//    }

    //Need to be finished
//    @Operation
//    fun getAll() {
//        cache.getAll()
//    }

    @Operation
    fun put(@Param(name = "key") key: Int, value: String = "v$key") {
        cache.put(key, value)
    }

    //Need to be finished
//    @Operation
//    fun putAll() {
//        cache.putAll()
//    }

    @Operation
    fun invalidate(@Param(name = "key") key: Int) {
        cache.invalidate(key)
    }

    //Turned off to make test faster
//    @Operation
//    fun invalidateAll() {
//        cache.invalidateAll()
//    }
//
//    @Operation
//    fun estimatedSize() {
//        cache.estimatedSize()
//    }
//
//    @Operation
//    fun stats() {
//        cache.stats()
//    }
//
//    @Operation
//    fun asMap() {
//        cache.asMap()
//    }
//
//    @Operation
//    fun cleanUp() {
//        cache.cleanUp()
//    }
//
//    @Operation
//    fun policy() {
//        cache.policy()
//    }


    @Test
    fun stressTest() {
        StressOptions().check(this::class)
    }

    @Test
    fun modelTest() {
        ModelCheckingOptions().check(this::class)
    }
}