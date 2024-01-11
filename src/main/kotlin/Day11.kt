import aoc.Day
import kotlin.math.abs

class Day11 : Day(11) {

    val universe = Grid(puzzle) {
        it.map { c -> c }
    }

    val voidCols: MutableList<Int>
    val voidLines: List<Int>

    val galaxies: List<Pair<Int, Int>>

    init {
        galaxies = universe.grid.mapIndexed { lIdx, line ->
            line.mapIndexed() { cIdx, c -> if (c == '#') Pair(lIdx, cIdx) else Pair(-1, -1) }
                .filter { pair -> pair != Pair(-1, -1) }
        }.flatten()
        val lines = universe.max.first
        voidCols = mutableListOf<Int>()
        for (i in 0..<universe.max.second) {
            if (universe.grid.filter { l -> l[i] == '.' }.size == lines) {
                voidCols += i
            }
        }
        voidLines = universe.grid.mapIndexed { i, l -> if (l.all { c -> c == '.' }) i else -1 }.filter { it > -1 }
    }

    fun solve(factor: Int = 1) {

        val a = galaxies.foldIndexed(0L) { index, outerAcc, from ->
            outerAcc + galaxies.drop(index + 1).fold(0L) { acc, to ->
                val expLines = calcExpanse(from.first, to.first, voidLines)
                val expCols = calcExpanse(from.second, to.second, voidCols)
                (acc + from.euclidenDistance(to)
                        + (expLines * factor - expLines)
                        + (expCols * factor - expCols)
                        )

                // if factor 1  part 1
//                (acc + from.euclidenDistance(to)
//                        + (expLines * factor)
//                        + (expCols * factor )
//                        )
            }
        }
        println(a)
    }

    fun calcExpanse(from: Int, to: Int, spaces: List<Int>): Int =
        if (abs(from - to) < 2) {
            0
        } else if (from < to) {
            spaces.filter {
                it in from ..to
            }.size
        } else {
            spaces.filter {
                it in to ..from
            }.size
        }

}

fun main() {
    val day = Day11()
    println(day.universe)
    day.solve(1000000)
}