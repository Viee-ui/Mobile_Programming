abstract class Dwelling(val residents: Int) {
    abstract val buildingMaterial: String
    abstract fun floorArea(): Double
}

open class RoundHut(
    residents: Int,
    val radius: Double
) : Dwelling(residents) {

    override val buildingMaterial = "Straw"

    override fun floorArea(): Double {
        return kotlin.math.PI * radius * radius
    }
}

class SquareCabin(
    residents: Int,
    val side: Double
) : Dwelling(residents) {

    override val buildingMaterial = "Wood"

    override fun floorArea(): Double {
        return side * side
    }
}

fun main() {
    val hut = RoundHut(4, 3.0)
    val cabin = SquareCabin(6, 5.0)

    println("Round hut area: ${hut.floorArea()}")
    println("Square cabin area: ${cabin.floorArea()}")
}
