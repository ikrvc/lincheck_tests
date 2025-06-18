import com.github.benmanes.caffeine.cache.Ticker
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

class ManualTicker : Ticker {
    private val nanos = AtomicLong()

    override fun read(): Long = nanos.get()

    fun advance(duration: Long, unit: TimeUnit) {
        nanos.addAndGet(unit.toNanos(duration))
    }
}
