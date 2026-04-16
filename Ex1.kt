fun main() {
    // In văn bản
    println("Hello, world!")
    println("This is the text to print!")

    // Comment
    // Đây là comment 1 dòng

    // Biến
    val age = 20          // không đổi
    val name = "Rover"

    var roll = 6          // có thể đổi
    var rolledValue: Int = 4

    // Chuỗi mẫu
    println("You are already $age!")
    println("You are already $age years old, $name!")

    // Gọi hàm
    printHello()

    // Hàm có đối số
    printBorder("*", 10)

    // Hàm trả về giá trị
    val diceResult = rollDice()
    println("Dice rolled: $diceResult")

    // If / else
    if (diceResult > 3) {
        println("High roll!")
    } else {
        println("Low roll!")
    }

    // When
    when (diceResult) {
        6 -> println("Excellent!")
        1 -> println("Too bad!")
        else -> println("Try again!")
    }
}

// Hàm không đối số
fun printHello() {
    println("Hello Kotlin")
}

// Hàm có đối số
fun printBorder(border: String, timesToRepeat: Int) {
    repeat(timesToRepeat) {
        print(border)
    }
    println()
}

// Hàm trả về giá trị
fun rollDice(): Int {
    return (1..6).random()
}
