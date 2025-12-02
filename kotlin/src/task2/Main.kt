package task2

import utils.Color
import java.io.File
import java.math.BigInteger
import kotlin.math.floor
import kotlin.math.sqrt
import utils.log

data class Range(val from: BigInteger, val to: BigInteger)

fun readFile(pathname: String): List<Range> {
    val text = File(pathname).readText()

    val ranges = mutableListOf<Range>()
    val parts = text.split(",")
    for (part in parts) {
        val rangeParts = part.split("-")
        require(rangeParts.size == 2) {
            "Invalid range"
        }

        val rangeFrom = rangeParts[0].toBigIntegerOrNull() ?: error("${rangeParts[0]} is not a valid 'from' integer")
        val rangeTo = rangeParts[1].toBigIntegerOrNull() ?: error("${rangeParts[1]} is not a valid 'to' integer")

        val range = Range(rangeFrom, rangeTo)
        ranges.add(range)
    }
    return ranges.toList()
}

fun isValid(id: BigInteger): Boolean {
    val s = id.abs().toString()

    if (s.length % 2 == 1) {
        return true
    }

    val midpoint = s.length / 2
    val firstHalf = s.take(midpoint)
    val secondHalf = s.substring(midpoint)

    return firstHalf.compareTo(secondHalf) != 0
}

// simple method for getting all factors of a number
fun getFactors(num: Int): List<Int> {
    val factors = mutableListOf<Int>()
    val n = floor(sqrt(num.toFloat())).toInt()

    for (index in 1..n) {
        if (num.mod(index) == 0) {
            // index is a divisor
            factors.add(index)
            // num / index is also a divisor
            factors.add(num / index)
        }
    }
    return factors.toList()
}

// for part 2
fun isValid2(id: BigInteger): Boolean {
    val s = id.abs().toString()

    if (s.length == 1)
        return true

    val midpoint = s.length / 2
    val lengthFactors = getFactors(s.length).filter { it <= midpoint }

    for (n in lengthFactors) {
        val part = s.take(n)
        val repeated = part.repeat(s.length / n)
        if (repeated.compareTo(s) == 0) {
            return false
        }
    }
    return true
}

fun getInvalidIdsInRange(range: Range, shouldLog: Boolean = false): List<BigInteger> {
    var current = range.from
    val invalidIds = mutableListOf<BigInteger>()
    while (current <= range.to) {
        val valid = isValid2(current)
        if (!valid) {
            if (shouldLog) log("$current is INVALID", Color.RED)
            invalidIds.add(current)
        } else {
            if (shouldLog) log("$current is VALID", Color.GREEN)
        }
        current++
    }
    return invalidIds.toList()
}

fun start() {
    val ranges = readFile("./src/task2/input")

    var sum = BigInteger.ZERO
    for (range in ranges) {
        val invalidIds = getInvalidIdsInRange(range)
        for (invalidId in invalidIds) {
            sum += invalidId
        }
    }

//    val range = Range(BigInteger("1188511885"), BigInteger("1188511890"))
//    getInvalidIdsInRange(range, true)

    println("Result: $sum")
}