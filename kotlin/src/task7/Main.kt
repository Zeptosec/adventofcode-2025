package task7

import java.io.File

fun readFile(pathname: String): List<String> = File(pathname).readLines()

class Teleporter(val data: List<String>) {

    fun getMap(): List<MutableList<Char>> {
        return data.map { line ->
            line.map { it }.toMutableList()
        }
    }

    // Reference
    // https://github.com/7UpMan/aoc2025/blob/61db34cf5606ef6e059b2b02880c05e073c75c43/src/main/java/day07/Day07.java#L97-L122
    fun process3() {
        val rowWidth = data[0].length
        val beamLocations = Array<Long>(rowWidth) { 0 }
        val startX = data[0].indexOf('S')
        var splitCount = 0

        require(startX in 0..<rowWidth) {
            error("Starting point not found!")
        }

        beamLocations[startX] = 1

        for (y in 1..<data.size) {
            for (x in 0..<data[y].length) {
                // if we found a splitter and we have a beam
                if (data[y][x] == '^' && beamLocations[x] > 0) {
                    // split the beam
                    beamLocations[x - 1] += beamLocations[x]
                    beamLocations[x + 1] += beamLocations[x]
                    beamLocations[x] = 0
                    splitCount++
                }
            }
        }

        val timelines = beamLocations.sum()
        println("splitCount: $splitCount, Timelines: $timelines")
    }

    // recursion takes too long when it's 140 deep
    /**
     * @deprecated
     */
    fun process2() {
        val map = getMap()

        var timelines = 0
        fun beamDown(sx: Int, sy: Int) {
            for (y in sy + 1..<map.size) {
                // split beam down
                if (map[y][sx] == '^') {
                    // beam left
                    if (sx > 0) {
                        beamDown(sx - 1, y)
                    }

                    // beam right
                    if (sx < map[y].size - 1) {
                        beamDown(sx + 1, y)
                    }

                    // the current beam dies
                    // why tf did I put continue here? I wrote 'it dies' and put continue like it still continues...
                    return
                }
            }
            timelines++
        }

        val sx = map[0].indexOf('S')
        beamDown(sx, 0)
        println("timelines: $timelines")
    }

    fun process1() {
        val map = getMap()

        var splitTimes = 0
        for (y in 0..<map.size) {
            for (x in 0..<map[y].size) {
                if (map[y][x] == 'S' && y < map.size - 1) {
                    map[y + 1][x] = '|'
                }

                if (y == 0) continue;

                // beaming down
                if (map[y][x] == '.' && map[y - 1][x] == '|') {
                    map[y][x] = '|'
                }

                // beam splitting
                if (map[y][x] == '^' && map[y - 1][x] == '|') {
                    if (x > 0) {
                        map[y][x - 1] = '|'
                    }
                    if (x < map[y].size - 1) {
                        map[y][x + 1] = '|'
                    }
                    splitTimes++
                }
            }
        }

        println("Split $splitTimes times")
    }
}

fun start() {
    val data = readFile("./src/task7/input")
    val teleporter = Teleporter(data)
    teleporter.process3()
}