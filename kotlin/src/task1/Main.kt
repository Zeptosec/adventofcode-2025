package task1

import java.io.File
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.max

enum class Direction {
    LEFT, RIGHT
}

data class Rotation(val direction: Direction, val amount: Int)

fun readRotations(pathname: String): List<Rotation> = File(pathname).useLines { lines ->
    lines.mapIndexed { index, line ->
        val trimmed = line.trim()
        require(trimmed.isNotEmpty()) {
            "Line ${index + 1} is blank!"
        }

        val directionChar = line.first()
        val direction = when (directionChar.uppercaseChar()) {
            'R' -> Direction.RIGHT
            'L' -> Direction.LEFT
            else -> error("Invalid direction: '$directionChar' at line ${index + 1}")
        }

        val amountStr = line.drop(1)
        val amount = amountStr.toIntOrNull()
            ?: error("Invalid rotation amount '$amountStr' at line: ${index + 1}")
        Rotation(direction, amount)
    }.toList()
}


class Dial(startingPoint: Int = 50, val dialMin: Int = 0, dialMax: Int = 99) {
    var point = startingPoint
        private set
    val distance = dialMax - dialMin + 1

    private fun mapPointToRange(point: Int) = (point - dialMin).mod(distance) + dialMin

    init {
        require(dialMin < dialMax) {
            "dialMax must be greater then dialMin."
        }
    }

    var zeroesCount = if (startingPoint == 0) 1 else 0
        private set

    fun rotate(rotation: Rotation) {
        // calculate amount to rotate
        val amount = rotation.amount * if (rotation.direction == Direction.LEFT) -1 else 1
        // save the starting point for calculations
        val startPoint = point
        // the new point on the line
        val endPoint = startPoint + amount
        // map the point to range between min and max
        point = mapPointToRange(endPoint)

        // for the part 2 calculate how many revolutions were done
        var revolutions = abs(floor((endPoint - dialMin).toDouble() / distance).toInt())

        // if the rotation starts from zero and goes in the left direction then remove one revolution
        if(startPoint == 0 && rotation.direction == Direction.LEFT) {
            revolutions = max(0, revolutions - 1)
        }

        // if it lands on zero then add a zero
        if (point == 0) {
            zeroesCount++
            // if it landed on zero, and it has done some revolutions then subtract one to prevent double counting only when going right
            if (rotation.direction == Direction.RIGHT)
                revolutions = max(0, revolutions - 1)
        }

        zeroesCount += revolutions
    }

    override fun toString(): String {
        return "Dial(point=$point)"
    }
}

fun start() {
    val dial = Dial()
    val rotations = readRotations("./src/task1/input")

    /// testing
//    val rots = arrayOf(
//        Rotation(Direction.LEFT, 68),
//        Rotation(Direction.LEFT, 30),
//        Rotation(Direction.RIGHT, 48),
//        Rotation(Direction.LEFT, 5),
//        Rotation(Direction.RIGHT, 60),
//        Rotation(Direction.LEFT, 55),
//        Rotation(Direction.LEFT, 1),
//        Rotation(Direction.LEFT, 99),
//        Rotation(Direction.RIGHT, 14),
//        Rotation(Direction.LEFT, 82),
//    )
//    for(rot in rots){
//        dial.rotate(rot)
//        println("Dial: $dial, Zeroes hit: ${dial.zeroesCount}")
//    }
    ///

    for (rotation in rotations) {
        dial.rotate(rotation)
    }

    println("Dial: $dial, Zeroes hit: ${dial.zeroesCount}")
}