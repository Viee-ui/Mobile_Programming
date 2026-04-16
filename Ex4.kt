import kotlinx.coroutines.*

suspend fun getValue(): Double {
    delay(1000)
    return Math.random() * 100
}

fun main() = runBlocking {
    println("Start")

    val job = launch {
        val value = getValue()
        println("Value: $value")
    }

    job.join()
    println("Done")
}
