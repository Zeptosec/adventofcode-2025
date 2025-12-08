package task8

import java.io.File
import kotlin.math.sqrt

class Point(val x: Long, val y: Long, val z: Long) {
    val nodes = mutableListOf<Point>()
    var circuit: Circuit? = null

    fun distanceTo(point: Point): Double {
        val dx = point.x - x
        val dy = point.y - y
        val dz = point.z - z
        val distanceSquared = dx * dx + dy * dy + dz * dz
        return sqrt(distanceSquared.toDouble())
    }
}

fun readFile(pathname: String) = File(pathname).useLines { lines ->
    lines.map { line ->
        val parts = line.split(",")
        require(parts.size == 3) {
            "Line '$line' is not a point"
        }
        Point(parts[0].toLong(), parts[1].toLong(), parts[2].toLong())
    }.toList()
}

class Vector(val point1: Point, val point2: Point) {
    val distance: Double by lazy {
        point1.distanceTo(point2)
    }
}

fun getVectors(points: List<Point>): List<Vector> {
    val vectors = mutableListOf<Vector>()
    for (i in 0..<points.size) {
        for (j in i + 1..<points.size) {
            vectors.add(
                Vector(points[i], points[j])
            )
        }
    }
    return vectors
}

class Circuit {
    val points = mutableListOf<Point>()

    fun add(point: Point) {
        // compare by reference
        val existingPoint = points.find { it == point }
        if (existingPoint == null) {
            points.add(point)
            point.circuit = this
        }
    }
}

fun process1(points: List<Point>) {
    val circuits = mutableListOf<Circuit>()
    val vectors = getVectors(points)

    val sortedVectors = vectors.sortedBy { it.distance }

    var lastConnection: Vector? = null

    for (i in 0..<sortedVectors.size) {
        val vector = sortedVectors[i]
        val point1 = vector.point1
        val point2 = vector.point2
        val cir1 = vector.point1.circuit
        val cir2 = vector.point2.circuit

        if (cir1 == null || cir2 == null || cir1 != cir2) {
            lastConnection = vector
        }

        if (cir1 == null && cir2 == null) {
            val cir = Circuit()
            circuits.add(cir)
            cir.add(point1)
            cir.add(point2)
        } else if (cir1 != null && cir2 == null) {
            cir1.add(point2)
        } else if (cir1 == null && cir2 != null) {
            cir2.add(point1)
        } else if (cir1 !== cir2 && cir1 !== null && cir2 != null) {
            // move all elements from box2 to box1 or vice versa

            for (point in cir2.points) {
                cir1.add(point)
                circuits.remove(cir2)
            }
        }
    }

    circuits.sortByDescending { it.points.size }
    val big3 = circuits.take(3).map { it.points.size }
    val result = big3.fold(1) { acc, count -> acc * count }
    println("The big 3 are: ${big3.joinToString(" * ")} = $result")

    if (lastConnection == null) {
        println("Failed to find last connection!")
    } else {
        val x1 = lastConnection.point1.x
        val x2 = lastConnection.point2.x
        val result = x1 * x2
        println("Last connection x vars: $x1 * $x2 = $result")
    }
}

fun start() {
    val points = readFile("./src/task8/input")
    process1(points)

}