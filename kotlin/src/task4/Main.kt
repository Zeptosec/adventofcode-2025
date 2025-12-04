package task4

import java.io.File
import kotlin.math.max
import kotlin.math.min

enum class Tile(val char: Char) {
    ROLL_OF_PAPER('@'),
    EMPTY('.'),
    REMOVED('x')
}

// in reality, you should not use mutable list for this kind of thing
// you should return a new mutated map instead
// but now this way is much faster to code
class Map(val mapTiles: MutableList<MutableList<Char>>) {

    fun getAmountOfRollsAround(x: Int, y: Int): Int {
        var amount = 0
        val fromX = max(0, x - 1)
        val toX = min(mapTiles[0].size - 1, x + 1)

        val fromY = max(0, y - 1)
        val toY = min(mapTiles.size - 1, y + 1)

        for (iy in fromY..toY) {
            for (ix in fromX..toX) {
                if (x == ix && iy == y) continue

                if (mapTiles[iy][ix] == Tile.ROLL_OF_PAPER.char) {
                    amount++
                }
            }
        }
        return amount
    }

    fun getAccessAmount(maxRollsOfPaperAround: Int): Int {
        var amount = 0
        for (y in 0..<mapTiles.size) {
            for (x in 0..<mapTiles[y].size) {
                if (mapTiles[y][x] != Tile.ROLL_OF_PAPER.char) continue

                val rollAmount = getAmountOfRollsAround(x, y)
                if (rollAmount <= maxRollsOfPaperAround) {
                    // indicate that it was removed
                    mapTiles[y][x] = Tile.REMOVED.char
                    amount++
                }
            }
        }
        return amount
    }
}

fun readFile(pathname: String) = File(pathname).useLines { lines ->
    lines.map { line ->
        line.map { char ->
            char
        }.toMutableList()
    }.toMutableList()
}


fun start() {
    val mapTiles = readFile("./src/task4/input")
    val map = Map(mapTiles)
    var totalCount = 0

    do {
        val count = map.getAccessAmount(3)
        totalCount += count
    } while (count > 0)

    println("$totalCount rolls can be accessed by forklift")
}