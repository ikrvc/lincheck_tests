import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.Test

class DoubleArrayEasyTest {

    private var myInt: Int = 0
    private var myBool: Boolean = false

    @Operation
    fun set() {
        if(myBool) {
            myBool = false
            myInt = 1
        }
        else{
            myBool = true
            myInt = 0
        }
    }

    @Operation
    fun setBool(bool: Boolean) {
        myBool = bool
    }

    @Operation
    fun get(): Int = myInt

    @Test
    fun stressTest() {
        StressOptions().check(this::class)
    }

    @Test
    fun modelTest() {
        ModelCheckingOptions().check(this::class)
    }
}