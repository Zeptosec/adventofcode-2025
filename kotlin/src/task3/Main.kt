package task3

import java.io.File
import kotlin.math.pow

private fun maxInd(list: List<Int>, fromIndex: Int = 0, toIndex: Int = list.size): IndexedValue<Int> {
    require(toIndex - fromIndex > 0) {
        "Cannot go from $fromIndex to $toIndex in a list. The range is ${fromIndex - toIndex} which is 0 or less!"
    }
    var maxed = IndexedValue(fromIndex, list[fromIndex])
    for (i in (fromIndex + 1)..<toIndex) {
        val value = list[i]
        if (maxed.value < value) {
            maxed = IndexedValue(i, value)
        }
    }
    return maxed
}

fun strToDigits(line: String): List<Int> {
    val trimmed = line.trim()
    return trimmed.toList().map { char ->
        char.digitToIntOrNull() ?: error("Char: '$char' is not a digit! in line \"$line\"")
    }.toList()
}

class Battery(val digits: List<Int>) {

    constructor(line: String) : this(strToDigits(line))

    fun maximumJoltage(digitCount: Int): List<Int> {
        var fromIndex = 0
        val startIndex = digits.size - digitCount + 1

        val resultList = mutableListOf<Int>()
        for (toIndex in startIndex..digits.size) {
            val max = maxInd(digits, fromIndex, toIndex)
            resultList.add(max.value)
            fromIndex = max.index + 1
        }
        return resultList
    }
}

fun readFile(pathname: String): List<Battery> = File(pathname).useLines { lines ->
    lines.map { Battery(it) }.toList()
}

fun process(digitCount: Int) {
    val batteries = readFile("./src/task3/input")

    val maxJoltage = batteries.sumOf { listToLong(it.maximumJoltage(digitCount)) }

    println("Maximum joltage: $maxJoltage")
}

fun listToLong(list: List<Int>): Long {
    var result: Long = 0
    val scale = 10.toDouble()
    list.asReversed().withIndex().forEach { (i, item) ->
        result += item * (scale.pow(i.toDouble()).toLong())
    }
    return result
}

fun start() {
    process(12)

//    val bat = Battery("818181911112111")
//    val max = bat.maximumJoltage(12)
//
//    var str = max.joinToString("")
//    println("Max joltage: $str -> ${listToLong(max)}")
}