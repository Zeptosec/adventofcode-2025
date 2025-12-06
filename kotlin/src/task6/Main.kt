package task6

import java.io.File

fun getOperationResult(nums: List<Long>, operator: String): Long {
    return when (operator) {
        "+" -> nums.sum()
        "*" -> nums.reduce { acc, value -> acc * value }
        else -> error("Unknown operator: '$operator'")
    }
}

class Sheet(val numbers: List<List<Long>>, val operators: List<String>) {
    fun getTotal(): Long {
        var total: Long = 0;
        for (i in 0..<operators.size) {
            val nums = numbers.map { it[i] }
            val result = getOperationResult(nums, operators[i])
            total += result
        }
        return total;
    }
}

fun readFile(pathname: String): Sheet {
    val lines = File(pathname).readLines()
    val numbers = lines.take(lines.size - 1).map { line ->
        val parts = line.split(" ").filter { it.isNotEmpty() }
        parts.map { it.toLong() }
    }

    val operators = lines.takeLast(1).map { line ->
        line.split(" ").filter { it.isNotEmpty() }
    }[0]

    return Sheet(numbers, operators)
}

fun parseNumbers2(section: List<String>): List<Long> {
    val numbers = mutableListOf<Long>()

    // basically start in the top right corner and go down write into a list all the characters and convert it to a long and return
    for (x in section[0].length - 1 downTo 0) {
        val chars = mutableListOf<Char>()
        for (y in 0..<section.size) {
            chars.add(section[y][x])
        }
        val number = chars.joinToString("").trim().toLong()
        numbers.add(number)
    }
    return numbers
}

fun process(pathname: String): Long {
    val lines = File(pathname).readLines()
    val maxLength = lines.maxOfOrNull { it.length }
    if (maxLength == null) {
        error("Could not find the max line")
    }

    val fixedLines = lines.map {
        if (it.length < maxLength) {
            val diff = maxLength - it.length
            it + " ".repeat(diff)
        } else {
            it
        }
    }

    var total: Long = 0
    var lastCursor = 0

    fun processRange(from: Int, to: Int): Long {
        val sliceRange = IntRange(from, to - 1)
        val operator = fixedLines[fixedLines.size - 1].substring(sliceRange).trim()
        val numbersSection = fixedLines.take(fixedLines.size - 1).map { it.substring(sliceRange) }
        val numbers = parseNumbers2(numbersSection)

        val result = getOperationResult(numbers, operator)
//        println("${numbers.joinToString(" $operator ")} = $result")
        return result
    }

    for (i in 1..<maxLength) {
        val column = fixedLines.map { it[i] }.joinToString("").trim()
        // if column is empty that means it's a separator
        if (column.isEmpty()) {
            val result = processRange(lastCursor, i)

            total += result
            lastCursor = i + 1
        }
    }
    val lastResult = processRange(lastCursor, maxLength)
    total += lastResult

    return total
}

fun start() {
//    val sheet = readFile2("./src/task6/input")
//    val total = sheet.getTotal()

    val total = process("./src/task6/input")
    println("Total result: $total")
}