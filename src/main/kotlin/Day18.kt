import aoc.Day
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


data class Edge(val from: Pair<Long, Long>, val to: Pair<Long, Long>, val color: String)

@OptIn(ExperimentalStdlibApi::class)
class Day18 : Day(18) {


    fun solve1(): Long {
        var lastPos = Pair(0L, 0L)
        val edges = puzzle.map { line ->
            val (dir, n, color) = line.split(" ")

            val to = when (dir) {
                "R" -> Pair(lastPos.first + n.toInt(), lastPos.second)
                "L" -> Pair(lastPos.first - n.toInt(), lastPos.second)
                "U" -> Pair(lastPos.first, lastPos.second - n.toInt())
                "D" -> Pair(lastPos.first, lastPos.second + n.toInt())
                else -> error("Unknown direction")
            }
            val edge = Edge(lastPos, to, color)
            lastPos = to
            edge
        }
        return edges.shoelace()
    }

    fun solve2(): Long {
        var lastPos = Pair(0L, 0L)
        val edges = puzzle.map { line ->
            val (_, _, color) = line.split(" ")
            val n = color.drop(2).take(5).hexToInt(format = HexFormat.Default)
            val to = when (color.dropLast(1).takeLast(1)) {
                "0" -> Pair(lastPos.first + n, lastPos.second)
                "1" -> Pair(lastPos.first, lastPos.second + n)
                "2" -> Pair(lastPos.first - n, lastPos.second)
                "3" -> Pair(lastPos.first, lastPos.second - n)
                else -> error("Unknown direction")
            }
            val edge = Edge(lastPos, to, color)
            lastPos = to
            edge
        }
        return edges.shoelace()
    }

    fun List<Edge>.shoelace(): Long =
        this.flatMap { listOf(it.from, it.to) }
            .zipWithNext { (x1, y1), (x2, y2) ->
                x1 * y2 - y1 * x2 + abs(x1 - x2) + abs(y1 - y2)
            } // shoelace algorithm + outer edges
            .sum()
            .div(2L)
            .plus(1L)

}


fun main() {
    val day = Day18()
    println(day.solve1())
    println(day.solve2())
}