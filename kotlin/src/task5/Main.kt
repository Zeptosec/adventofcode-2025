package task5

import java.io.File

data class Range(var from: Long, var to: Long)

fun inRange(range: Range, value: Long): Boolean {
    return value in range.from..range.to
}

fun findRange(ranges: List<Range>, value: Long): Range? {
    for (range in ranges) {
        if (inRange(range, value)) {
            return range
        }
    }
    return null
}

class Timeline(ranges: List<Range>) {
    val mergedRanges: List<Range>

    init {
        val timeline = mutableListOf<Range>()
        val ascendingRanges = ranges.sortedBy { it.from }

        for (range in ascendingRanges) {
            val timelineRange = findRange(timeline, range.from)
            // we want to check if the "from" value is in the range and the "to" value is not in range.
            // if both "from" and "to" are in range that means we have a subset of range which we can discard it.
            // else we want to extend the range to the "to" mark
            if (timelineRange != null) {
                if (!inRange(timelineRange, range.to)) {
                    timelineRange.to = range.to
                }
            } else {
                // make a copy to avoid mutating original data
                timeline.add(range.copy())
            }
        }
        mergedRanges = timeline
    }

    fun getRangesDistance(): Long {
        var count: Long = 0
        for (mergedRange in mergedRanges) {
            count += mergedRange.to - mergedRange.from + 1
        }
        return count
    }
}

class Database(val ranges: List<Range>, val ingredients: List<Long>) {

    fun getFreshCount(): Int {
        var count = 0
        for (ingredient in ingredients) {
            for (range in ranges) {
                if (inRange(range, ingredient)) {
                    count++
                    break
                }
            }
        }
        return count
    }
}

fun readFile(pathname: String): Database {
    val ingredients = mutableListOf<Long>()
    val ranges = mutableListOf<Range>()
    File(pathname).useLines { lines ->
        for (line in lines) {
            val trimmed = line.trim()

            if (trimmed.isEmpty()) continue

            if (trimmed.contains('-')) {
                val rangeParts = trimmed.split('-')
                require(rangeParts.size == 2) {
                    "Invalid range '$trimmed'"
                }

                val from = rangeParts[0].toLong()
                val to = rangeParts[1].toLong()

                require(to >= from) {
                    "Invalid range. $from is bigger $to"
                }

                ranges.add(
                    Range(from, to)
                )
            } else {
                ingredients.add(trimmed.toLong())
            }
        }
    }
    return Database(ranges.toList(), ingredients.toList())
}

fun start() {
    val database = readFile("./src/task5/input")
//    val freshCount = database.getFreshCount()

//    println("Fresh count: $freshCount")

//    val database = Database(
//        listOf(
//            Range(1, 10),
//            Range(4, 6),
//            Range(11, 12),
//            Range(12, 14)
//        ),
//        listOf()
//    )
    val timeline = Timeline(database.ranges)
    val maxFreshCount = timeline.getRangesDistance()
    println("$maxFreshCount are considered to be fresh")
}