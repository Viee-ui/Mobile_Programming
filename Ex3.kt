fun main() {
    // List
    val numbers = listOf(0, 3, 8, 4, 0, 5, 5, 8, 9, 2)
    println("List size: ${numbers.size}")
    println("First element: ${numbers[0]}")

    // Set
    val setOfNumbers = numbers.toSet()
    println("Set: $setOfNumbers")

    val set1 = setOf(1, 2, 3)
    val set2 = mutableSetOf(3, 4, 5)
    println("Intersect: ${set1.intersect(set2)}")
    println("Union: ${set1.union(set2)}")

    // Map
    val peopleAges = mutableMapOf(
        "Fred" to 30,
        "Ann" to 23
    )

    peopleAges["Barbara"] = 42
    peopleAges["Joe"] = 51

    peopleAges.forEach {
        println("${it.key} is ${it.value}")
    }

    // Filter
    val filteredNames = peopleAges.filter { it.key.length < 4 }
    println("Filtered: $filteredNames")

    // Chuỗi thao tác
    val words = listOf("about", "acute", "balloon", "best", "brief", "class")
    val result = words.filter { it.startsWith("b") }
        .shuffled()
        .take(2)
        .sorted()

    println(result)
}
